package com.agentmesh.router.dto;

public class ScoredQuoteDto {
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

    public ScoredQuoteDto() {}

    public ScoredQuoteDto(String id, String agentId, String agentName, Double price, Integer estimatedTimeSeconds, Double confidence, Double successRate, Double rating, Double score, Boolean selected) {
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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
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

        public Builder id(String id) { this.id = id; return this; }
        public Builder agentId(String agentId) { this.agentId = agentId; return this; }
        public Builder agentName(String agentName) { this.agentName = agentName; return this; }
        public Builder price(Double price) { this.price = price; return this; }
        public Builder estimatedTimeSeconds(Integer estimatedTimeSeconds) { this.estimatedTimeSeconds = estimatedTimeSeconds; return this; }
        public Builder confidence(Double confidence) { this.confidence = confidence; return this; }
        public Builder successRate(Double successRate) { this.successRate = successRate; return this; }
        public Builder rating(Double rating) { this.rating = rating; return this; }
        public Builder score(Double score) { this.score = score; return this; }
        public Builder selected(Boolean selected) { this.selected = selected; return this; }

        public ScoredQuoteDto build() {
            return new ScoredQuoteDto(id, agentId, agentName, price, estimatedTimeSeconds, confidence, successRate, rating, score, selected);
        }
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
