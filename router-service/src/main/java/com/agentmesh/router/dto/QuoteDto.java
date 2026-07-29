package com.agentmesh.router.dto;

public class QuoteDto {
    private String id;
    private String agentId;
    private String agentName;
    private Double price;
    private Integer estimatedTimeSeconds;
    private Double confidence;
    private Double successRate;
    private Double rating;
    private Double score;
    private Boolean selected;

    public QuoteDto() {}

    public QuoteDto(String id, String agentId, String agentName, Double price, Integer estimatedTimeSeconds, Double confidence, Double successRate, Double rating, Double score, Boolean selected) {
        this.id = id;
        this.agentId = agentId;
        this.agentName = agentName;
        this.price = price;
        this.estimatedTimeSeconds = estimatedTimeSeconds;
        this.confidence = confidence;
        this.successRate = successRate;
        this.rating = rating;
        this.score = score;
        this.selected = selected;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getEstimatedTimeSeconds() { return estimatedTimeSeconds; }
    public void setEstimatedTimeSeconds(Integer estimatedTimeSeconds) { this.estimatedTimeSeconds = estimatedTimeSeconds; }
    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public Boolean getSelected() { return selected; }
    public void setSelected(Boolean selected) { this.selected = selected; }
}
