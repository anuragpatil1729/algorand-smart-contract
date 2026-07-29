package com.agentmesh.router.dto;

import java.util.List;

public class QuoteResponseDto {
    private String agentId;
    private String agentName;
    private Double price;
    private Integer estimatedTime;
    private Double confidence;
    private Double successRate;
    private Double rating;
    private List<String> supportedCapabilities;

    public QuoteResponseDto() {}

    public QuoteResponseDto(String agentId, String agentName, Double price, Integer estimatedTime, Double confidence, Double successRate, Double rating, List<String> supportedCapabilities) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.price = price;
        this.estimatedTime = estimatedTime;
        this.confidence = confidence;
        this.successRate = successRate;
        this.rating = rating;
        this.supportedCapabilities = supportedCapabilities;
    }

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(Integer estimatedTime) { this.estimatedTime = estimatedTime; }
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public List<String> getSupportedCapabilities() { return supportedCapabilities; }
    public void setSupportedCapabilities(List<String> supportedCapabilities) { this.supportedCapabilities = supportedCapabilities; }
}
