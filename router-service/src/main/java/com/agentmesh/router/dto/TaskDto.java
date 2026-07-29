package com.agentmesh.router.dto;

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
    private List<QuoteDto> quotes;

    public TaskDto() {}

    public TaskDto(String id, String workflowId, String taskType, String description, String assignedAgent, String status, Double price, List<String> dependencies, Integer priority, String estimatedComplexity, Long executionTimeMs, String output, List<QuoteDto> quotes) {
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
    public List<QuoteDto> getQuotes() { return quotes; }
    public void setQuotes(List<QuoteDto> quotes) { this.quotes = quotes; }
}
