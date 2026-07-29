package com.agentmesh.router.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "quoted_price", nullable = false)
    private Double quotedPrice = 0.0;

    @Column(nullable = false)
    private Double confidence = 95.0;

    @Column(name = "estimated_time", nullable = false)
    private Integer estimatedTime = 10;

    @Column(name = "reputation_score", nullable = false)
    private Double reputationScore = 4.8;

    @Column(nullable = false)
    private Double score = 0.0;

    private Boolean selected = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Quote() {}

    public Quote(String id, Workflow workflow, Agent agent, String taskId, Double quotedPrice, Double confidence, Integer estimatedTime, Double reputationScore, Double score, Boolean selected, LocalDateTime createdAt) {
        this.id = id;
        this.workflow = workflow;
        this.agent = agent;
        this.taskId = taskId;
        this.quotedPrice = quotedPrice != null ? quotedPrice : 0.0;
        this.confidence = confidence != null ? confidence : 95.0;
        this.estimatedTime = estimatedTime != null ? estimatedTime : 10;
        this.reputationScore = reputationScore != null ? reputationScore : 4.8;
        this.score = score != null ? score : 0.0;
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
        private Workflow workflow;
        private Agent agent;
        private String taskId;
        private Double quotedPrice = 0.0;
        private Double confidence = 95.0;
        private Integer estimatedTime = 10;
        private Double reputationScore = 4.8;
        private Double score = 0.0;
        private Boolean selected = false;
        private LocalDateTime createdAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder workflow(Workflow workflow) { this.workflow = workflow; return this; }
        public Builder workflowId(String workflowId) {
            if (workflowId != null) this.workflow = Workflow.builder().id(workflowId).build();
            return this;
        }

        public Builder agent(Agent agent) { this.agent = agent; return this; }
        public Builder agentId(String agentId) {
            if (agentId != null) this.agent = Agent.builder().id(agentId).build();
            return this;
        }

        public Builder taskId(String taskId) { this.taskId = taskId; return this; }
        public Builder quotedPrice(Double quotedPrice) { this.quotedPrice = quotedPrice; return this; }
        public Builder price(Double price) { this.quotedPrice = price; return this; }
        public Builder confidence(Double confidence) { this.confidence = confidence; return this; }
        public Builder estimatedTime(Integer estimatedTime) { this.estimatedTime = estimatedTime; return this; }
        public Builder estimatedTimeSeconds(Integer seconds) { this.estimatedTime = seconds; return this; }
        public Builder reputationScore(Double reputationScore) { this.reputationScore = reputationScore; return this; }
        public Builder rating(Double rating) {
            if (rating != null) this.reputationScore = rating;
            return this;
        }
        public Builder successRate(Double successRate) { return this; }
        public Builder score(Double score) { this.score = score; return this; }
        public Builder selected(Boolean selected) { this.selected = selected; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Quote build() {
            return new Quote(id, workflow, agent, taskId, quotedPrice, confidence, estimatedTime, reputationScore, score, selected, createdAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Workflow getWorkflow() { return workflow; }
    public void setWorkflow(Workflow workflow) { this.workflow = workflow; }

    public String getWorkflowId() { return workflow != null ? workflow.getId() : null; }
    public void setWorkflowId(String workflowId) {
        if (workflowId != null) this.workflow = Workflow.builder().id(workflowId).build();
    }

    public Agent getAgent() { return agent; }
    public void setAgent(Agent agent) { this.agent = agent; }

    public String getAgentId() { return agent != null ? agent.getId() : null; }
    public void setAgentId(String agentId) {
        if (agentId != null) this.agent = Agent.builder().id(agentId).build();
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public Double getQuotedPrice() { return quotedPrice; }
    public void setQuotedPrice(Double quotedPrice) { this.quotedPrice = quotedPrice; }

    public Double getPrice() { return quotedPrice; }
    public void setPrice(Double price) { this.quotedPrice = price; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }
    public Integer getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(Integer estimatedTime) { this.estimatedTime = estimatedTime; }

    public Integer getEstimatedTimeSeconds() { return estimatedTime; }
    public void setEstimatedTimeSeconds(Integer estimatedTimeSeconds) { this.estimatedTime = estimatedTimeSeconds; }

    public Double getReputationScore() { return reputationScore; }
    public void setReputationScore(Double reputationScore) { this.reputationScore = reputationScore; }

    public Double getRating() { return reputationScore; }
    public void setRating(Double rating) { this.reputationScore = rating; }

    public Double getSuccessRate() { return 95.0; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public Boolean getSelected() { return selected; }
    public void setSelected(Boolean selected) { this.selected = selected; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
