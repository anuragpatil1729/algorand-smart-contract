package com.agentmesh.router.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TaskDto {
    private String id;
    private String workflowId;
    private String taskType;
    private String description;
    private String assignedAgent;
    private String status;
    private Double price;
    private List<String> dependencies;
    private Integer priority;
    private String estimatedComplexity;
    private Long executionTimeMs;
    private String output;
    private List<ScoredQuoteDto> quotes;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public TaskDto() {}

    public TaskDto(String id, String workflowId, String taskType, String description, String assignedAgent, String status, Double price, List<String> dependencies, Integer priority, String estimatedComplexity, Long executionTimeMs, String output, List<ScoredQuoteDto> quotes, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.workflowId = workflowId;
        this.taskType = taskType;
        this.description = description;
        this.assignedAgent = assignedAgent;
        this.status = status;
        this.price = price;
        this.dependencies = dependencies;
        this.priority = priority;
        this.estimatedComplexity = estimatedComplexity;
        this.executionTimeMs = executionTimeMs;
        this.output = output;
        this.quotes = quotes;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String workflowId;
        private String taskType;
        private String description;
        private String assignedAgent;
        private String status;
        private Double price;
        private List<String> dependencies;
        private Integer priority;
        private String estimatedComplexity;
        private Long executionTimeMs;
        private String output;
        private List<ScoredQuoteDto> quotes;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder workflowId(String workflowId) { this.workflowId = workflowId; return this; }
        public Builder taskType(String taskType) { this.taskType = taskType; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder assignedAgent(String assignedAgent) { this.assignedAgent = assignedAgent; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder price(Double price) { this.price = price; return this; }
        public Builder dependencies(List<String> dependencies) { this.dependencies = dependencies; return this; }
        public Builder priority(Integer priority) { this.priority = priority; return this; }
        public Builder estimatedComplexity(String estimatedComplexity) { this.estimatedComplexity = estimatedComplexity; return this; }
        public Builder executionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; return this; }
        public Builder output(String output) { this.output = output; return this; }
        public Builder quotes(List<ScoredQuoteDto> quotes) { this.quotes = quotes; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public TaskDto build() {
            return new TaskDto(id, workflowId, taskType, description, assignedAgent, status, price, dependencies, priority, estimatedComplexity, executionTimeMs, output, quotes, createdAt, completedAt);
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
    public List<String> getDependencies() { return dependencies; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getEstimatedComplexity() { return estimatedComplexity; }
    public void setEstimatedComplexity(String estimatedComplexity) { this.estimatedComplexity = estimatedComplexity; }
    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
    public List<ScoredQuoteDto> getQuotes() { return quotes; }
    public void setQuotes(List<ScoredQuoteDto> quotes) { this.quotes = quotes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
