package com.agentmesh.router.planner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PlannedTaskDto {

    @JsonProperty("taskId")
    private String taskId;

    @JsonProperty("taskName")
    private String taskName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("taskType")
    private String taskType;

    @JsonProperty("requiredCapability")
    private String requiredCapability;

    @JsonProperty("priority")
    private Integer priority = 1;

    @JsonProperty("estimatedDurationSeconds")
    private Integer estimatedDurationSeconds = 10;

    @JsonProperty("estimatedCost")
    private Double estimatedCost = 50.0;

    @JsonProperty("complexity")
    private String complexity = "MEDIUM";

    @JsonProperty("dependencies")
    private List<String> dependencies;

    @JsonProperty("executionStage")
    private Integer executionStage = 1;

    @JsonProperty("retryPolicy")
    private String retryPolicy = "MAX_RETRIES_3";

    @JsonProperty("validationRules")
    private List<String> validationRules;

    @JsonProperty("status")
    private String status = "PLANNED";

    public PlannedTaskDto() {}

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public String getRequiredCapability() { return requiredCapability; }
    public void setRequiredCapability(String requiredCapability) { this.requiredCapability = requiredCapability; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getEstimatedDurationSeconds() { return estimatedDurationSeconds; }
    public void setEstimatedDurationSeconds(Integer estimatedDurationSeconds) { this.estimatedDurationSeconds = estimatedDurationSeconds; }
    public Double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(Double estimatedCost) { this.estimatedCost = estimatedCost; }
    public String getComplexity() { return complexity; }
    public void setComplexity(String complexity) { this.complexity = complexity; }
    public List<String> getDependencies() { return dependencies; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    public Integer getExecutionStage() { return executionStage; }
    public void setExecutionStage(Integer executionStage) { this.executionStage = executionStage; }
    public String getRetryPolicy() { return retryPolicy; }
    public void setRetryPolicy(String retryPolicy) { this.retryPolicy = retryPolicy; }
    public List<String> getValidationRules() { return validationRules; }
    public void setValidationRules(List<String> validationRules) { this.validationRules = validationRules; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
