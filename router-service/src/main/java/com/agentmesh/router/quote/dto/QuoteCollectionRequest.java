package com.agentmesh.router.quote.dto;

import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;

public class QuoteCollectionRequest {

    private String workflowId;
    private WorkflowPlanResponseDto workflowPlan;

    public QuoteCollectionRequest() {}

    public QuoteCollectionRequest(String workflowId, WorkflowPlanResponseDto workflowPlan) {
        this.workflowId = workflowId;
        this.workflowPlan = workflowPlan;
    }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public WorkflowPlanResponseDto getWorkflowPlan() { return workflowPlan; }
    public void setWorkflowPlan(WorkflowPlanResponseDto workflowPlan) { this.workflowPlan = workflowPlan; }
}
