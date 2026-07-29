package com.agentmesh.router.dto;

public class QuoteResponse {
    private String id;
    private String workflowId;
    private String agentId;
    private String agentName;
    private Double quotedPrice;
    private Double confidence;
    private Integer estimatedTime;
    private Double reputationScore;

    public QuoteResponse() {}

    public QuoteResponse(String id, String workflowId, String agentId, String agentName, Double quotedPrice, Double confidence, Integer estimatedTime, Double reputationScore) {
        this.id = id;
        this.workflowId = workflowId;
        this.agentId = agentId;
        this.agentName = agentName;
        this.quotedPrice = quotedPrice;
        this.confidence = confidence;
        this.estimatedTime = estimatedTime;
        this.reputationScore = reputationScore;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public Double getQuotedPrice() { return quotedPrice; }
    public void setQuotedPrice(Double quotedPrice) { this.quotedPrice = quotedPrice; }
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    public Integer getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(Integer estimatedTime) { this.estimatedTime = estimatedTime; }
    public Double getReputationScore() { return reputationScore; }
    public void setReputationScore(Double reputationScore) { this.reputationScore = reputationScore; }
}
