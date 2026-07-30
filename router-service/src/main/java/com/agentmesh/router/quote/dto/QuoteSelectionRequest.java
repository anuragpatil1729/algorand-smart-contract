package com.agentmesh.router.quote.dto;

import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import java.util.Map;

public class QuoteSelectionRequest {

    private String workflowId;
    private WorkflowPlanResponseDto workflowPlan;
    private String strategy = "BALANCED";
    private Map<String, Double> customWeights;

    public QuoteSelectionRequest() {}

    public QuoteSelectionRequest(String workflowId, WorkflowPlanResponseDto workflowPlan, String strategy, Map<String, Double> customWeights) {
        this.workflowId = workflowId;
        this.workflowPlan = workflowPlan;
        this.strategy = strategy != null ? strategy : "BALANCED";
        this.customWeights = customWeights;
    }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public WorkflowPlanResponseDto getWorkflowPlan() { return workflowPlan; }
    public void setWorkflowPlan(WorkflowPlanResponseDto workflowPlan) { this.workflowPlan = workflowPlan; }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

    public Map<String, Double> getCustomWeights() { return customWeights; }
    public void setCustomWeights(Map<String, Double> customWeights) { this.customWeights = customWeights; }
}
