package com.agentmesh.router.orchestration.dto;

import com.agentmesh.router.execution.dto.WorkflowExecutionStatusDto;
import com.agentmesh.router.execution.dto.WorkflowResult;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import com.agentmesh.router.quote.dto.TaskAssignment;
import com.agentmesh.router.quote.dto.TaskQuoteSummary;
import com.agentmesh.router.x402.dto.X402Receipt;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class UnifiedWorkflowResponse {

    @JsonProperty("workflowId")
    private String workflowId;

    @JsonProperty("executionId")
    private String executionId;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("receipt")
    private X402Receipt receipt;

    @JsonProperty("executionTimeMs")
    private Long executionTimeMs;

    @JsonProperty("plannerOutput")
    private WorkflowPlanResponseDto plannerOutput;

    @JsonProperty("selectedAgents")
    private List<TaskAssignment> selectedAgents;

    @JsonProperty("quoteSummary")
    private List<TaskQuoteSummary> quoteSummary;

    @JsonProperty("executionSummary")
    private WorkflowExecutionStatusDto executionSummary;

    @JsonProperty("result")
    private WorkflowResult result;

    @JsonProperty("timeline")
    private WorkflowTimeline timeline;

    @JsonProperty("validationReport")
    private Map<String, Object> validationReport;

    public UnifiedWorkflowResponse() {}

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public X402Receipt getReceipt() { return receipt; }
    public void setReceipt(X402Receipt receipt) { this.receipt = receipt; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public WorkflowPlanResponseDto getPlannerOutput() { return plannerOutput; }
    public void setPlannerOutput(WorkflowPlanResponseDto plannerOutput) { this.plannerOutput = plannerOutput; }

    public List<TaskAssignment> getSelectedAgents() { return selectedAgents; }
    public void setSelectedAgents(List<TaskAssignment> selectedAgents) { this.selectedAgents = selectedAgents; }

    public List<TaskQuoteSummary> getQuoteSummary() { return quoteSummary; }
    public void setQuoteSummary(List<TaskQuoteSummary> quoteSummary) { this.quoteSummary = quoteSummary; }

    public WorkflowExecutionStatusDto getExecutionSummary() { return executionSummary; }
    public void setExecutionSummary(WorkflowExecutionStatusDto executionSummary) { this.executionSummary = executionSummary; }

    public WorkflowResult getResult() { return result; }
    public void setResult(WorkflowResult result) { this.result = result; }

    public WorkflowTimeline getTimeline() { return timeline; }
    public void setTimeline(WorkflowTimeline timeline) { this.timeline = timeline; }

    public Map<String, Object> getValidationReport() { return validationReport; }
    public void setValidationReport(Map<String, Object> validationReport) { this.validationReport = validationReport; }
}
