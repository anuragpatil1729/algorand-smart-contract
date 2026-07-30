package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionTaskResponse;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RetryManager {

    private static final Logger log = LoggerFactory.getLogger(RetryManager.class);

    public enum RetryPolicy {
        IMMEDIATE,
        FIXED_DELAY,
        EXPONENTIAL_BACKOFF
    }

    private final int defaultMaxAttempts;
    private final long defaultDelayMs;

    public RetryManager(
            @Value("${agentmesh.execution.max-retries:2}") int defaultMaxRetries,
            @Value("${agentmesh.execution.retry-delay-ms:500}") long defaultDelayMs
    ) {
        this.defaultMaxAttempts = defaultMaxRetries + 1;
        this.defaultDelayMs = defaultDelayMs;
    }

    public ExecutionTaskResponse executeWithRetry(
            TaskAssignment assignment,
            ExecutionContext context,
            TaskExecutor taskExecutor,
            TimeoutManager timeoutManager,
            Long customTimeoutMs,
            Integer customMaxRetries,
            RetryPolicy retryPolicy
    ) {
        int maxAttempts = (customMaxRetries != null && customMaxRetries >= 0) ? customMaxRetries + 1 : defaultMaxAttempts;
        RetryPolicy policy = retryPolicy != null ? retryPolicy : RetryPolicy.FIXED_DELAY;
        String taskId = assignment.getTaskId();

        ExecutionTaskResponse response = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            if (attempt > 1) {
                log.info("Initiating retry attempt {}/{} for task '{}' (policy: {})", attempt, maxAttempts, taskId, policy);
                context.updateTaskState(taskId, ExecutionStateMachine.TaskState.RETRYING);
                applyBackoff(attempt, policy);
            }

            response = timeoutManager.executeWithTimeout(assignment, context, taskExecutor, customTimeoutMs);

            if (isSuccessful(response)) {
                return response;
            }

            log.warn("Task '{}' attempt {} failed with status '{}': {}", taskId, attempt, response.getStatus(), response.getError());
        }

        return response;
    }

    public boolean isSuccessful(ExecutionTaskResponse response) {
        if (response == null) return false;
        String status = response.getStatus();
        return "COMPLETED".equalsIgnoreCase(status) || "SUCCESS".equalsIgnoreCase(status);
    }

    private void applyBackoff(int attempt, RetryPolicy policy) {
        if (policy == RetryPolicy.IMMEDIATE) return;

        long sleepMs = defaultDelayMs;
        if (policy == RetryPolicy.EXPONENTIAL_BACKOFF) {
            sleepMs = (long) (defaultDelayMs * Math.pow(2, attempt - 2));
        }

        try {
            Thread.sleep(Math.min(sleepMs, 5000L));
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
