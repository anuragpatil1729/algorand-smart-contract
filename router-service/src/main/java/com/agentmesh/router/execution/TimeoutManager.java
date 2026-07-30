package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionTaskResponse;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class TimeoutManager {

    private static final Logger log = LoggerFactory.getLogger(TimeoutManager.class);

    private final ExecutorService timeoutExecutor;
    private final long defaultTimeoutMs;

    public TimeoutManager(@Value("${agentmesh.execution.task-timeout-ms:30000}") long defaultTimeoutMs) {
        this.defaultTimeoutMs = defaultTimeoutMs;
        this.timeoutExecutor = Executors.newCachedThreadPool();
    }

    public ExecutionTaskResponse executeWithTimeout(TaskAssignment assignment, ExecutionContext context, TaskExecutor taskExecutor, Long customTimeoutMs) {
        long timeoutMs = customTimeoutMs != null && customTimeoutMs > 0 ? customTimeoutMs : defaultTimeoutMs;
        String taskId = assignment.getTaskId();

        Future<ExecutionTaskResponse> future = timeoutExecutor.submit(() -> taskExecutor.executeTask(assignment, context));

        try {
            return future.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException te) {
            log.warn("Task execution timed out after {}ms for task '{}' on agent '{}'", timeoutMs, taskId, assignment.getSelectedAgentId());
            future.cancel(true);
            return new ExecutionTaskResponse(
                    "exec-timeout-" + System.currentTimeMillis(),
                    taskId,
                    "TIMEOUT",
                    null,
                    timeoutMs,
                    "Task execution timed out after " + timeoutMs + "ms"
            );
        } catch (Exception e) {
            log.error("Execution error during timeout handling for task '{}': {}", taskId, e.getMessage());
            future.cancel(true);
            return new ExecutionTaskResponse(
                    "exec-error-" + System.currentTimeMillis(),
                    taskId,
                    "FAILED",
                    null,
                    0L,
                    "Execution error: " + e.getMessage()
            );
        }
    }
}
