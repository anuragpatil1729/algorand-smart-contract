package com.agentmesh.router.quote.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AgentQuoteResponse {

    @JsonProperty("agentId")
    @JsonAlias({"agentId", "agent_id", "id"})
    private String agentId;

    @JsonProperty("agentName")
    @JsonAlias({"agentName", "agent_name", "name"})
    private String agentName;

    @JsonProperty("taskId")
    @JsonAlias({"taskId", "task_id"})
    private String taskId;

    @JsonProperty("workflowId")
    @JsonAlias({"workflowId", "workflow_id"})
    private String workflowId;

    @JsonProperty("capability")
    @JsonAlias({"capability", "requiredCapability", "required_capability"})
    private String capability;

    @JsonProperty("supportedCapabilities")
    @JsonAlias({"supportedCapabilities", "supported_capabilities", "capabilities"})
    private List<String> supportedCapabilities;

    @JsonProperty("quotedPrice")
    @JsonAlias({"quotedPrice", "quoted_price", "price", "basePrice"})
    private Double quotedPrice = 0.0;

    @JsonProperty("estimatedDuration")
    @JsonAlias({"estimatedDuration", "estimated_duration", "estimatedTime", "estimated_time", "eta"})
    private Integer estimatedDuration = 10;

    @JsonProperty("confidence")
    private Double confidence = 95.0;

    @JsonProperty("currentQueueLength")
    @JsonAlias({"currentQueueLength", "current_queue_length", "queueLength", "queue_size", "queueSize"})
    private Integer currentQueueLength = 0;

    @JsonProperty("currentLoad")
    @JsonAlias({"currentLoad", "current_load", "load", "loadPercentage"})
    private Double currentLoad = 0.0;

    @JsonProperty("averageResponseTime")
    @JsonAlias({"averageResponseTime", "average_response_time", "responseTimeMs", "response_time_ms"})
    private Double averageResponseTime = 50.0;

    @JsonProperty("successRate")
    @JsonAlias({"successRate", "success_rate"})
    private Double successRate = 98.0;

    @JsonProperty("reputation")
    @JsonAlias({"reputation", "reputationScore", "reputation_score", "rating"})
    private Double reputation = 4.8;

    @JsonProperty("healthScore")
    @JsonAlias({"healthScore", "health_score", "health"})
    private Double healthScore = 100.0;

    @JsonProperty("timestamp")
    private Long timestamp = System.currentTimeMillis();

    @JsonProperty("score")
    private Double score = 0.0;

    @JsonProperty("valid")
    private Boolean valid = true;

    @JsonProperty("status")
    private String status = "SUCCESS";

    @JsonProperty("rejectionReason")
    private String rejectionReason;

    public AgentQuoteResponse() {}

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getCapability() { return capability; }
    public void setCapability(String capability) { this.capability = capability; }

    public List<String> getSupportedCapabilities() { return supportedCapabilities; }
    public void setSupportedCapabilities(List<String> supportedCapabilities) { this.supportedCapabilities = supportedCapabilities; }

    public Double getQuotedPrice() { return quotedPrice; }
    public void setQuotedPrice(Double quotedPrice) { this.quotedPrice = quotedPrice; }

    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }

    public Integer getCurrentQueueLength() { return currentQueueLength; }
    public void setCurrentQueueLength(Integer currentQueueLength) { this.currentQueueLength = currentQueueLength; }

    public Double getCurrentLoad() { return currentLoad; }
    public void setCurrentLoad(Double currentLoad) { this.currentLoad = currentLoad; }

    public Double getAverageResponseTime() { return averageResponseTime; }
    public void setAverageResponseTime(Double averageResponseTime) { this.averageResponseTime = averageResponseTime; }

    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }

    public Double getReputation() { return reputation; }
    public void setReputation(Double reputation) { this.reputation = reputation; }

    public Double getHealthScore() { return healthScore; }
    public void setHealthScore(Double healthScore) { this.healthScore = healthScore; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public Boolean getValid() { return valid; }
    public void setValid(Boolean valid) { this.valid = valid; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
