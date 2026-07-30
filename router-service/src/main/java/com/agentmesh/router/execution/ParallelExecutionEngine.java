package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionTaskResponse;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class ParallelExecutionEngine {

    private static final Logger log = LoggerFactory.getLogger(ParallelExecutionEngine.class);

    private final ExecutorService executorService;
    private final int defaultMaxConcurrency;

    public ParallelExecutionEngine(@Value("${agentmesh.execution.max-concurrency:10}") int maxConcurrency) {
        this.defaultMaxConcurrency = maxConcurrency;
        this.executorService = Executors.newFixedThreadPool(maxConcurrency);
    }

    public Map<String, ExecutionTaskResponse> executeTasksConcurrently(
            List<TaskAssignment> readyTasks,
            ExecutionContext context,
            java.util.function.Function<TaskAssignment, ExecutionTaskResponse> taskExecutionFunction
    ) {
        if (readyTasks == null || readyTasks.isEmpty()) {
            return Collections.emptyMap();
        }

        log.info("ParallelExecutionEngine executing {} independent tasks concurrently in workflow '{}'", readyTasks.size(), context.getWorkflowId());

        Map<String, ExecutionTaskResponse> results = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = readyTasks.stream()
                .map(assignment -> CompletableFuture.runAsync(() -> {
                    String taskId = assignment.getTaskId();
                    context.updateTaskState(taskId, ExecutionStateMachine.TaskState.RUNNING);
                    ExecutionTaskResponse response = taskExecutionFunction.apply(assignment);
                    results.put(taskId, response);
                }, executorService))
                .collect(Collectors.toList());

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            log.error("Exception during parallel task execution in workflow '{}': {}", context.getWorkflowId(), e.getMessage());
        }

        return results;
    }
}
