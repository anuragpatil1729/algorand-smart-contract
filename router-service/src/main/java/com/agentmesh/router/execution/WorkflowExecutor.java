package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionEvent;
import com.agentmesh.router.execution.dto.WorkflowExecutionRequest;
import com.agentmesh.router.execution.dto.WorkflowResult;
import com.agentmesh.router.model.enums.LogLevel;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkflowExecutor {

    private static final Logger log = LoggerFactory.getLogger(WorkflowExecutor.class);

    private final ExecutionCoordinator coordinator;
    private final ExecutionStateMachine stateMachine;
    private final ExecutionEventBus eventBus;
    private final ExecutionLogger executionLogger;
    private final ExecutionValidator validator;
    private final ExecutionResultAggregator resultAggregator;
    private final ExecutionHistoryService historyService;

    public WorkflowExecutor(
            ExecutionCoordinator coordinator,
            ExecutionStateMachine stateMachine,
            ExecutionEventBus eventBus,
            ExecutionLogger executionLogger,
            ExecutionValidator validator,
            ExecutionResultAggregator resultAggregator,
            ExecutionHistoryService historyService
    ) {
        this.coordinator = coordinator;
        this.stateMachine = stateMachine;
        this.eventBus = eventBus;
        this.executionLogger = executionLogger;
        this.validator = validator;
        this.resultAggregator = resultAggregator;
        this.historyService = historyService;
    }

    public WorkflowResult executeWorkflowSync(WorkflowExecutionRequest request) {
        if (request == null || request.getAssignmentPlan() == null) {
            throw new IllegalArgumentException("WorkflowExecutionRequest and AssignmentPlan must not be null");
        }

        AssignmentPlan plan = request.getAssignmentPlan();
        String workflowId = request.getWorkflowId() != null ? request.getWorkflowId() :
                (plan.getWorkflowId() != null ? plan.getWorkflowId() : UUID.randomUUID().toString());

        ExecutionContext context = new ExecutionContext(workflowId, plan);
        historyService.storeContext(context);

        stateMachine.transitionWorkflow(context.getWorkflowState(), ExecutionStateMachine.WorkflowState.QUEUED);
        context.setWorkflowState(ExecutionStateMachine.WorkflowState.QUEUED);

        stateMachine.transitionWorkflow(context.getWorkflowState(), ExecutionStateMachine.WorkflowState.EXECUTION_STARTED);
        context.setWorkflowState(ExecutionStateMachine.WorkflowState.EXECUTION_STARTED);

        eventBus.publishEvent(new ExecutionEvent("WORKFLOW_STARTED", workflowId, null, null, "Started workflow execution with strategy: " + plan.getSelectionStrategyUsed()), context);
        executionLogger.logEvent(context, null, null, LogLevel.INFO, "Workflow execution started for workflowId: " + workflowId);

        stateMachine.transitionWorkflow(context.getWorkflowState(), ExecutionStateMachine.WorkflowState.RUNNING);
        context.setWorkflowState(ExecutionStateMachine.WorkflowState.RUNNING);

        Map<String, List<String>> taskDepsMap = extractTaskDependencies(plan);

        boolean success = coordinator.executeWorkflowCoordinator(
                context, taskDepsMap, request.getTaskTimeoutMs(), request.getMaxRetries()
        );

        context.setCompletedTime(System.currentTimeMillis());

        if (success) {
            stateMachine.transitionWorkflow(context.getWorkflowState(), ExecutionStateMachine.WorkflowState.COMPLETED);
            context.setWorkflowState(ExecutionStateMachine.WorkflowState.COMPLETED);

            Map<String, Object> valReport = validator.validateWorkflowCompletion(context);
            WorkflowResult result = resultAggregator.aggregateResults(context, valReport);

            historyService.storeResult(workflowId, result);

            eventBus.publishEvent(new ExecutionEvent("WORKFLOW_COMPLETED", workflowId, null, null, "Workflow completed successfully in " + result.getTotalExecutionTimeMs() + "ms"), context);
            executionLogger.logEvent(context, null, null, LogLevel.INFO, "Workflow completed successfully");

            return result;
        } else {
            stateMachine.transitionWorkflow(context.getWorkflowState(), ExecutionStateMachine.WorkflowState.FAILED);
            context.setWorkflowState(ExecutionStateMachine.WorkflowState.FAILED);

            Map<String, Object> valReport = validator.validateWorkflowCompletion(context);
            WorkflowResult result = resultAggregator.aggregateResults(context, valReport);
            result.setStatus("FAILED");

            historyService.storeResult(workflowId, result);

            eventBus.publishEvent(new ExecutionEvent("WORKFLOW_FAILED", workflowId, null, null, "Workflow execution failed due to unrecoverable task errors"), context);
            executionLogger.logEvent(context, null, null, LogLevel.ERROR, "Workflow execution failed");

            return result;
        }
    }

    @Async("workflowExecutor")
    public CompletableFuture<WorkflowResult> executeWorkflowAsync(WorkflowExecutionRequest request) {
        return CompletableFuture.completedFuture(executeWorkflowSync(request));
    }

    public boolean cancelWorkflow(String workflowId) {
        ExecutionContext context = historyService.getContext(workflowId);
        if (context == null) return false;

        synchronized (context) {
            ExecutionStateMachine.WorkflowState current = context.getWorkflowState();
            if (current == ExecutionStateMachine.WorkflowState.RUNNING || current == ExecutionStateMachine.WorkflowState.PAUSED || current == ExecutionStateMachine.WorkflowState.QUEUED) {
                context.setWorkflowState(ExecutionStateMachine.WorkflowState.CANCELLED);
                eventBus.publishEvent(new ExecutionEvent("WORKFLOW_CANCELLED", workflowId, null, null, "Workflow execution cancelled by user request"), context);
                executionLogger.logEvent(context, null, null, LogLevel.WARN, "Workflow cancelled by user");
                return true;
            }
        }
        return false;
    }

    public boolean pauseWorkflow(String workflowId) {
        ExecutionContext context = historyService.getContext(workflowId);
        if (context == null) return false;

        synchronized (context) {
            if (context.getWorkflowState() == ExecutionStateMachine.WorkflowState.RUNNING) {
                stateMachine.transitionWorkflow(context.getWorkflowState(), ExecutionStateMachine.WorkflowState.PAUSED);
                context.setWorkflowState(ExecutionStateMachine.WorkflowState.PAUSED);
                eventBus.publishEvent(new ExecutionEvent("WORKFLOW_PAUSED", workflowId, null, null, "Workflow execution paused"), context);
                executionLogger.logEvent(context, null, null, LogLevel.INFO, "Workflow paused");
                return true;
            }
        }
        return false;
    }

    public boolean resumeWorkflow(String workflowId) {
        ExecutionContext context = historyService.getContext(workflowId);
        if (context == null) return false;

        synchronized (context) {
            if (context.getWorkflowState() == ExecutionStateMachine.WorkflowState.PAUSED) {
                stateMachine.transitionWorkflow(context.getWorkflowState(), ExecutionStateMachine.WorkflowState.RUNNING);
                context.setWorkflowState(ExecutionStateMachine.WorkflowState.RUNNING);
                eventBus.publishEvent(new ExecutionEvent("WORKFLOW_RESUMED", workflowId, null, null, "Workflow execution resumed"), context);
                executionLogger.logEvent(context, null, null, LogLevel.INFO, "Workflow resumed");
                return true;
            }
        }
        return false;
    }

    private Map<String, List<String>> extractTaskDependencies(AssignmentPlan plan) {
        Map<String, List<String>> map = new ConcurrentHashMap<>();
        if (plan == null || plan.getAssignments() == null) return map;

        // By default, if tasks don't specify explicit dependencies, order sequentially or by capability
        List<String> taskIds = new ArrayList<>();
        plan.getAssignments().forEach(a -> taskIds.add(a.getTaskId()));

        for (int i = 0; i < taskIds.size(); i++) {
            String currentId = taskIds.get(i);
            if (i == 0) {
                map.put(currentId, Collections.emptyList());
            } else {
                // Dependency on previous task in chain
                map.put(currentId, List.of(taskIds.get(i - 1)));
            }
        }
        return map;
    }
}
