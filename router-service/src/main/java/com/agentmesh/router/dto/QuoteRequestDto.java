package com.agentmesh.router.dto;

public class QuoteRequestDto {
    private String taskId;
    private String taskType;
    private String description;
    private String estimatedComplexity;

    public QuoteRequestDto() {}

    public QuoteRequestDto(String taskId, String taskType, String description, String estimatedComplexity) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.description = description;
        this.estimatedComplexity = estimatedComplexity;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEstimatedComplexity() { return estimatedComplexity; }
    public void setEstimatedComplexity(String estimatedComplexity) { this.estimatedComplexity = estimatedComplexity; }
}
