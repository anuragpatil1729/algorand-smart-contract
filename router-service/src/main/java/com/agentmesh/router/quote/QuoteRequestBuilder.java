package com.agentmesh.router.quote;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.quote.dto.AgentQuoteRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class QuoteRequestBuilder {

    public AgentQuoteRequest buildQuoteRequest(PlannedTaskDto task, String workflowId) {
        if (task == null) return new AgentQuoteRequest();

        Map<String, Object> context = new HashMap<>();
        context.put("executionStage", task.getExecutionStage());
        context.put("retryPolicy", task.getRetryPolicy());
        if (task.getDependencies() != null) {
            context.put("dependencies", task.getDependencies());
        }

        return new AgentQuoteRequest(
                task.getTaskId(),
                workflowId,
                task.getDescription() != null ? task.getDescription() : task.getTaskName(),
                task.getTaskType(),
                task.getRequiredCapability(),
                task.getPriority() != null ? String.valueOf(task.getPriority()) : "1",
                task.getComplexity() != null ? task.getComplexity() : "MEDIUM",
                task.getEstimatedCost(),
                null,
                context
        );
    }
}
