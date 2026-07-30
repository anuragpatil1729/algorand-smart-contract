package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionEvent;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ExecutionMonitor {

    private final AtomicInteger activeWorkflows = new AtomicInteger(0);
    private final AtomicInteger activeTasks = new AtomicInteger(0);
    private final AtomicInteger totalWorkflowsStarted = new AtomicInteger(0);
    private final AtomicInteger totalWorkflowsCompleted = new AtomicInteger(0);
    private final AtomicInteger totalWorkflowsFailed = new AtomicInteger(0);
    private final AtomicInteger totalTaskFailures = new AtomicInteger(0);
    private final AtomicInteger totalRetriesCount = new AtomicInteger(0);
    private final AtomicInteger totalFallbackCount = new AtomicInteger(0);
    private final AtomicLong totalExecutionTimeMs = new AtomicLong(0);

    public ExecutionMonitor(ExecutionEventBus eventBus) {
        if (eventBus != null) {
            eventBus.registerListener(this::onEvent);
        }
    }

    public void onEvent(ExecutionEvent event) {
        if (event == null || event.getEventType() == null) return;

        switch (event.getEventType()) {
            case "WORKFLOW_STARTED":
                activeWorkflows.incrementAndGet();
                totalWorkflowsStarted.incrementAndGet();
                break;
            case "WORKFLOW_COMPLETED":
                activeWorkflows.decrementAndGet();
                totalWorkflowsCompleted.incrementAndGet();
                break;
            case "WORKFLOW_FAILED":
                activeWorkflows.decrementAndGet();
                totalWorkflowsFailed.incrementAndGet();
                break;
            case "TASK_STARTED":
                activeTasks.incrementAndGet();
                break;
            case "TASK_COMPLETED":
                activeTasks.decrementAndGet();
                break;
            case "TASK_FAILED":
                activeTasks.decrementAndGet();
                totalTaskFailures.incrementAndGet();
                break;
            case "RETRY_STARTED":
                totalRetriesCount.incrementAndGet();
                break;
            case "FALLBACK_STARTED":
                totalFallbackCount.incrementAndGet();
                break;
        }
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        int completed = totalWorkflowsCompleted.get();
        int failed = totalWorkflowsFailed.get();
        int total = completed + failed;

        double successRate = total > 0 ? (completed * 100.0 / total) : 100.0;

        metrics.put("activeWorkflows", Math.max(0, activeWorkflows.get()));
        metrics.put("activeTasks", Math.max(0, activeTasks.get()));
        metrics.put("totalWorkflowsStarted", totalWorkflowsStarted.get());
        metrics.put("totalWorkflowsCompleted", completed);
        metrics.put("totalWorkflowsFailed", failed);
        metrics.put("successRatePercentage", Math.round(successRate * 10.0) / 10.0);
        metrics.put("totalTaskFailures", totalTaskFailures.get());
        metrics.put("totalRetriesCount", totalRetriesCount.get());
        metrics.put("totalFallbackCount", totalFallbackCount.get());

        return metrics;
    }
}
