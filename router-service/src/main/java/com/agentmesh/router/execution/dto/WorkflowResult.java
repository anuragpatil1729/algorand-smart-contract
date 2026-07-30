package com.agentmesh.router.execution.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowResult {

    private String workflowId;
    private String status;
    private String aggregatedOutput;
    private Map<String, Object> taskOutputs = new HashMap<>();
    private List<ExecutionTaskResponse> taskResults = new ArrayList<>();
    private Long totalExecutionTimeMs = 0L;
    private Map<String, Object> validationReport = new HashMap<>();
    private Long completedAt = System.currentTimeMillis();

    public WorkflowResult() {}

    public WorkflowResult(String workflowId, String status) {
        this.workflowId = workflowId;
        this.status = status;
    }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAggregatedOutput() { return aggregatedOutput; }
    public void setAggregatedOutput(String aggregatedOutput) { this.aggregatedOutput = aggregatedOutput; }

    public Map<String, Object> getTaskOutputs() { return taskOutputs; }
    public void setTaskOutputs(Map<String, Object> taskOutputs) { this.taskOutputs = taskOutputs; }

    public List<ExecutionTaskResponse> getTaskResults() { return taskResults; }
    public void setTaskResults(List<ExecutionTaskResponse> taskResults) { this.taskResults = taskResults; }

    public Long getTotalExecutionTimeMs() { return totalExecutionTimeMs; }
    public void setTotalExecutionTimeMs(Long totalExecutionTimeMs) { this.totalExecutionTimeMs = totalExecutionTimeMs; }

    public Map<String, Object> getValidationReport() { return validationReport; }
    public void setValidationReport(Map<String, Object> validationReport) { this.validationReport = validationReport; }

    public Long getCompletedAt() { return completedAt; }
    public void setCompletedAt(Long completedAt) { this.completedAt = completedAt; }
}
