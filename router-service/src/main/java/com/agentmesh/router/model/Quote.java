package com.agentmesh.router.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    private String id;

    @Column(name = "task_id", nullable = false)
    private String taskId;

    @Column(name = "agent_id", nullable = false)
    private String agentId;

    @Column(nullable = false)
    private Double price;

    @Column(name = "estimated_time_seconds", nullable = false)
    private Integer estimatedTimeSeconds;

    @Column(nullable = false)
    private Double confidence;

    @Column(name = "success_rate", nullable = false)
    private Double successRate;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Double score;

    private Boolean selected = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Quote() {}

    public Quote(String id, String taskId, String agentId, Double price, Integer estimatedTimeSeconds, Double confidence, Double successRate, Double rating, Double score, Boolean selected, LocalDateTime createdAt) {
        this.id = id;
        this.taskId = taskId;
        this.agentId = agentId;
        this.price = price;
        this.estimatedTimeSeconds = estimatedTimeSeconds;
        this.confidence = confidence;
        this.successRate = successRate;
        this.rating = rating;
        this.score = score;
        this.selected = selected != null ? selected : false;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String taskId;
        private String agentId;
        private Double price;
        private Integer estimatedTimeSeconds;
        private Double confidence;
        private Double successRate;
        private Double rating;
        private Double score;
        private Boolean selected = false;
        private LocalDateTime createdAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder taskId(String taskId) { this.taskId = taskId; return this; }
        public Builder agentId(String agentId) { this.agentId = agentId; return this; }
        public Builder price(Double price) { this.price = price; return this; }
        public Builder estimatedTimeSeconds(Integer estimatedTimeSeconds) { this.estimatedTimeSeconds = estimatedTimeSeconds; return this; }
        public Builder confidence(Double confidence) { this.confidence = confidence; return this; }
        public Builder successRate(Double successRate) { this.successRate = successRate; return this; }
        public Builder rating(Double rating) { this.rating = rating; return this; }
        public Builder score(Double score) { this.score = score; return this; }
        public Builder selected(Boolean selected) { this.selected = selected; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Quote build() {
            return new Quote(id, taskId, agentId, price, estimatedTimeSeconds, confidence, successRate, rating, score, selected, createdAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
