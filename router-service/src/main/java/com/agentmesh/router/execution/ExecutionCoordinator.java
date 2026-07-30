package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionEvent;
import com.agentmesh.router.execution.dto.ExecutionTaskResponse;
import com.agentmesh.router.model.enums.LogLevel;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExecutionCoordinator {

    private static final Logger log = LoggerFactory.getLogger(ExecutionCoordinator.class);

    private final DependencyScheduler dependencyScheduler;
    private final ParallelExecutionEngine parallelExecutionEngine;
    private final TaskExecutor taskExecutor;
    private final TimeoutManager timeoutManager;
    private final RetryManager retryManager;
    private final FallbackManager fallbackManager;
    private final ExecutionEventBus eventBus;
    private final ExecutionLogger executionLogger;
    private final ExecutionStateMachine stateMachine;

    public ExecutionCoordinator(
            DependencyScheduler dependencyScheduler,
            ParallelExecutionEngine parallelExecutionEngine,
            TaskExecutor taskExecutor,
            TimeoutManager timeoutManager,
            RetryManager retryManager,
            FallbackManager fallbackManager,
            ExecutionEventBus eventBus,
            ExecutionLogger executionLogger,
            ExecutionStateMachine stateMachine
    ) {
        this.dependencyScheduler = dependencyScheduler;
        this.parallelExecutionEngine = parallelExecutionEngine;
        this.taskExecutor = taskExecutor;
        this.timeoutManager = timeoutManager;
        this.retryManager = retryManager;
        this.fallbackManager = fallbackManager;
        this.eventBus = eventBus;
        this.executionLogger = executionLogger;
        this.stateMachine = stateMachine;
    }

    public boolean executeWorkflowCoordinator(ExecutionContext context, Map<String, List<String>> taskDependenciesMap, Long taskTimeoutMs, Integer maxRetries) {
        if (context == null || context.getAssignmentPlan() == null) {
            return false;
        }

        String workflowId = context.getWorkflowId();
        int totalTasks = context.getAssignmentPlan().getAssignments().size();
        Map<String, Set<String>> failedAgentsMap = new ConcurrentHashMap<>();

        while (context.getCompletedTasks().size() < totalTasks && context.getWorkflowState() == ExecutionStateMachine.WorkflowState.RUNNING) {
            List<TaskAssignment> readyTasks = dependencyScheduler.getReadyTasks(context, taskDependenciesMap);

            if (readyTasks.isEmpty()) {
                if (!context.getFailedTasks().isEmpty()) {
                    log.error("Workflow '{}' failed due to unrecoverable task failures: {}", workflowId, context.getFailedTasks());
                    return false;
                }
                if (context.getCompletedTasks().size() < totalTasks) {
                    log.warn("Deadlock or unmet dependencies detected for workflow '{}'", workflowId);
                    return false;
                }
                break;
            }

            // Execute ready independent tasks concurrently
            Map<String, ExecutionTaskResponse> stageResults = parallelExecutionEngine.executeTasksConcurrently(
                    readyTasks,
                    context,
                    assignment -> executeSingleTaskWithRetryAndFallback(assignment, context, failedAgentsMap, taskTimeoutMs, maxRetries)
            );

            // Check for failures in current stage
            boolean stageFailed = false;
            for (Map.Entry<String, ExecutionTaskResponse> entry : stageResults.entrySet()) {
                if (!retryManager.isSuccessful(entry.getValue())) {
                    stageFailed = true;
                    context.updateTaskState(entry.getKey(), ExecutionStateMachine.TaskState.FAILED_PERMANENTLY);
                }
            }

            if (stageFailed) {
                return false;
            }
        }

        return context.getCompletedTasks().size() == totalTasks;
    }

    private ExecutionTaskResponse executeSingleTaskWithRetryAndFallback(
            TaskAssignment assignment,
            ExecutionContext context,
            Map<String, Set<String>> failedAgentsMap,
            Long taskTimeoutMs,
            Integer maxRetries
    ) {
        String taskId = assignment.getTaskId();
        String agentId = assignment.getSelectedAgentId();

        eventBus.publishEvent(new ExecutionEvent("TASK_STARTED", context.getWorkflowId(), taskId, agentId, "Started executing task: " + assignment.getTaskName()), context);
        executionLogger.logEvent(context, taskId, agentId, LogLevel.INFO, "Executing task '" + assignment.getTaskName() + "' on agent " + agentId);

        // 1. Try execution with retries
        ExecutionTaskResponse response = retryManager.executeWithRetry(
                assignment, context, taskExecutor, timeoutManager, taskTimeoutMs, maxRetries, RetryManager.RetryPolicy.FIXED_DELAY
        );

        if (retryManager.isSuccessful(response)) {
            context.updateTaskState(taskId, ExecutionStateMachine.TaskState.COMPLETED);
            context.setTaskOutput(taskId, response.getOutput());
            context.setTaskResponse(taskId, response);

            eventBus.publishEvent(new ExecutionEvent("TASK_COMPLETED", context.getWorkflowId(), taskId, agentId, "Task completed successfully by " + agentId), context);
            executionLogger.logEvent(context, taskId, agentId, LogLevel.INFO, "Task '" + taskId + "' completed successfully");
            return response;
        }

        // 2. Failure on primary agent -> Trigger Fallback Manager
        eventBus.publishEvent(new ExecutionEvent("TASK_FAILED", context.getWorkflowId(), taskId, agentId, "Primary execution failed on agent " + agentId), context);
        executionLogger.logEvent(context, taskId, agentId, LogLevel.WARN, "Primary execution failed on agent " + agentId + ". Initiating dynamic fallback.");

        Set<String> failedAgents = failedAgentsMap.computeIfAbsent(taskId, k -> ConcurrentHashMap.newKeySet());
        if (agentId != null) failedAgents.add(agentId);

        context.updateTaskState(taskId, ExecutionStateMachine.TaskState.FALLBACK);
        eventBus.publishEvent(new ExecutionEvent("FALLBACK_STARTED", context.getWorkflowId(), taskId, agentId, "Searching for candidate fallback agent"), context);

        Optional<TaskAssignment> fallbackAssignmentOpt = fallbackManager.findFallbackAssignment(assignment, context, failedAgents);

        if (fallbackAssignmentOpt.isPresent()) {
            TaskAssignment fallbackAssignment = fallbackAssignmentOpt.get();
            String fallbackAgentId = fallbackAssignment.getSelectedAgentId();
            context.updateTaskAssignment(taskId, fallbackAssignment);

            eventBus.publishEvent(new ExecutionEvent("FALLBACK_COMPLETED", context.getWorkflowId(), taskId, fallbackAgentId, "Assigned fallback agent: " + fallbackAgentId), context);
            executionLogger.logEvent(context, taskId, fallbackAgentId, LogLevel.INFO, "Re-assigned task '" + taskId + "' to fallback agent " + fallbackAgentId);

            // Execute on fallback agent
            ExecutionTaskResponse fallbackResponse = retryManager.executeWithRetry(
                    fallbackAssignment, context, taskExecutor, timeoutManager, taskTimeoutMs, maxRetries, RetryManager.RetryPolicy.IMMEDIATE
            );

            if (retryManager.isSuccessful(fallbackResponse)) {
                context.updateTaskState(taskId, ExecutionStateMachine.TaskState.COMPLETED);
                context.setTaskOutput(taskId, fallbackResponse.getOutput());
                context.setTaskResponse(taskId, fallbackResponse);

                eventBus.publishEvent(new ExecutionEvent("TASK_COMPLETED", context.getWorkflowId(), taskId, fallbackAgentId, "Task completed by fallback agent " + fallbackAgentId), context);
                executionLogger.logEvent(context, taskId, fallbackAgentId, LogLevel.INFO, "Task '" + taskId + "' completed successfully by fallback agent");
                return fallbackResponse;
            }
        }

        // Permanent failure if fallback also fails or no candidate available
        context.updateTaskState(taskId, ExecutionStateMachine.TaskState.FAILED_PERMANENTLY);
        eventBus.publishEvent(new ExecutionEvent("TASK_FAILED_PERMANENTLY", context.getWorkflowId(), taskId, agentId, "Task failed permanently. No fallback succeeded."), context);
        executionLogger.logEvent(context, taskId, agentId, LogLevel.ERROR, "Task '" + taskId + "' failed permanently after retries and fallback attempts.");

        return response;
    }
}
