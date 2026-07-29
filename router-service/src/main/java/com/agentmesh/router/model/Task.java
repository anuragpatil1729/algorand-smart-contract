package com.agentmesh.router.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    private String id;

    @Column(name = "workflow_id", nullable = false)
    private String workflowId;

    @Column(name = "task_type", nullable = false)
    private String taskType;

    @Column(length = 1000)
    private String description;

    @Column(name = "assigned_agent")
    private String assignedAgent;

    @Column(nullable = false)
    private String status;

    private Double price = 0.0;

    @Column(length = 500)
    private String dependencies;

    private Integer priority = 1;

    @Column(name = "estimated_complexity")
    private String estimatedComplexity = "MEDIUM";

    @Column(name = "execution_time_ms")
    private Long executionTimeMs = 0L;

    @Column(length = 10000)
    private String output;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Task() {}

    public Task(String id, String workflowId, String taskType, String description, String assignedAgent, String status, Double price, String dependencies, Integer priority, String estimatedComplexity, Long executionTimeMs, String output, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.workflowId = workflowId;
        this.taskType = taskType;
        this.description = description;
        this.assignedAgent = assignedAgent;
        this.status = status;
        this.price = price != null ? price : 0.0;
        this.dependencies = dependencies;
        this.priority = priority != null ? priority : 1;
        this.estimatedComplexity = estimatedComplexity != null ? estimatedComplexity : "MEDIUM";
        this.executionTimeMs = executionTimeMs != null ? executionTimeMs : 0L;
        this.output = output;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String workflowId;
        private String taskType;
        private String description;
        private String assignedAgent;
        private String status;
        private Double price = 0.0;
        private String dependencies;
        private Integer priority = 1;
        private String estimatedComplexity = "MEDIUM";
        private Long executionTimeMs = 0L;
        private String output;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder workflowId(String workflowId) { this.workflowId = workflowId; return this; }
        public Builder taskType(String taskType) { this.taskType = taskType; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder assignedAgent(String assignedAgent) { this.assignedAgent = assignedAgent; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder price(Double price) { this.price = price; return this; }
        public Builder dependencies(String dependencies) { this.dependencies = dependencies; return this; }
        public Builder priority(Integer priority) { this.priority = priority; return this; }
        public Builder estimatedComplexity(String estimatedComplexity) { this.estimatedComplexity = estimatedComplexity; return this; }
        public Builder executionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; return this; }
        public Builder output(String output) { this.output = output; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public Task build() {
            return new Task(id, workflowId, taskType, description, assignedAgent, status, price, dependencies, priority, estimatedComplexity, executionTimeMs, output, createdAt, completedAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAssignedAgent() { return assignedAgent; }
    public void setAssignedAgent(String assignedAgent) { this.assignedAgent = assignedAgent; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getDependencies() { return dependencies; }
    public void setDependencies(String dependencies) { this.dependencies = dependencies; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getEstimatedComplexity() { return estimatedComplexity; }
    public void setEstimatedComplexity(String estimatedComplexity) { this.estimatedComplexity = estimatedComplexity; }
    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
