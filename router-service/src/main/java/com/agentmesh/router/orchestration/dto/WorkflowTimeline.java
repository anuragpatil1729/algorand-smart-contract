package com.agentmesh.router.orchestration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkflowTimeline {

    @JsonProperty("planningStarted")
    private Long planningStarted;

    @JsonProperty("planningCompleted")
    private Long planningCompleted;

    @JsonProperty("discoveryCompleted")
    private Long discoveryCompleted;

    @JsonProperty("quoteCollectionCompleted")
    private Long quoteCollectionCompleted;

    @JsonProperty("assignmentCompleted")
    private Long assignmentCompleted;

    @JsonProperty("paymentVerified")
    private Long paymentVerified;

    @JsonProperty("executionStarted")
    private Long executionStarted;

    @JsonProperty("executionCompleted")
    private Long executionCompleted;

    public WorkflowTimeline() {
        this.planningStarted = System.currentTimeMillis();
    }

    public Long getPlanningStarted() { return planningStarted; }
    public void setPlanningStarted(Long planningStarted) { this.planningStarted = planningStarted; }

    public Long getPlanningCompleted() { return planningCompleted; }
    public void setPlanningCompleted(Long planningCompleted) { this.planningCompleted = planningCompleted; }

    public Long getDiscoveryCompleted() { return discoveryCompleted; }
    public void setDiscoveryCompleted(Long discoveryCompleted) { this.discoveryCompleted = discoveryCompleted; }

    public Long getQuoteCollectionCompleted() { return quoteCollectionCompleted; }
    public void setQuoteCollectionCompleted(Long quoteCollectionCompleted) { this.quoteCollectionCompleted = quoteCollectionCompleted; }

    public Long getAssignmentCompleted() { return assignmentCompleted; }
    public void setAssignmentCompleted(Long assignmentCompleted) { this.assignmentCompleted = assignmentCompleted; }

    public Long getPaymentVerified() { return paymentVerified; }
    public void setPaymentVerified(Long paymentVerified) { this.paymentVerified = paymentVerified; }

    public Long getExecutionStarted() { return executionStarted; }
    public void setExecutionStarted(Long executionStarted) { this.executionStarted = executionStarted; }

    public Long getExecutionCompleted() { return executionCompleted; }
    public void setExecutionCompleted(Long executionCompleted) { this.executionCompleted = executionCompleted; }
}
