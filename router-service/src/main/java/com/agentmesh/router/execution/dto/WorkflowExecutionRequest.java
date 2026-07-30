package com.agentmesh.router.execution.dto;

import com.agentmesh.router.quote.dto.AssignmentPlan;

public class WorkflowExecutionRequest {

    private String workflowId;
    private AssignmentPlan assignmentPlan;
    private Integer maxConcurrency = 5;
    private Long taskTimeoutMs = 30000L;
    private Integer maxRetries = 2;

    public WorkflowExecutionRequest() {}

    public WorkflowExecutionRequest(String workflowId, AssignmentPlan assignmentPlan) {
        this.workflowId = workflowId;
        this.assignmentPlan = assignmentPlan;
    }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public AssignmentPlan getAssignmentPlan() { return assignmentPlan; }
    public void setAssignmentPlan(AssignmentPlan assignmentPlan) { this.assignmentPlan = assignmentPlan; }

    public Integer getMaxConcurrency() { return maxConcurrency; }
    public void setMaxConcurrency(Integer maxConcurrency) { this.maxConcurrency = maxConcurrency; }

    public Long getTaskTimeoutMs() { return taskTimeoutMs; }
    public void setTaskTimeoutMs(Long taskTimeoutMs) { this.taskTimeoutMs = taskTimeoutMs; }

    public Integer getMaxRetries() { return maxRetries; }
    public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
}
