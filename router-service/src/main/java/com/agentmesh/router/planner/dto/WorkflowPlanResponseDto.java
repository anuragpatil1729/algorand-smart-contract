package com.agentmesh.router.planner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class WorkflowPlanResponseDto {

    @JsonProperty("workflowId")
    private String workflowId;

    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("taskList")
    private List<PlannedTaskDto> taskList;

    @JsonProperty("executionStages")
    private Map<Integer, List<PlannedTaskDto>> executionStages;

    @JsonProperty("parallelGroups")
    private List<List<String>> parallelGroups;

    @JsonProperty("totalEstimatedDurationSeconds")
    private Integer totalEstimatedDurationSeconds;

    @JsonProperty("totalEstimatedCost")
    private Double totalEstimatedCost;

    @JsonProperty("requiredCapabilities")
    private List<String> requiredCapabilities;

    @JsonProperty("graphRepresentation")
    private Map<String, Object> graphRepresentation;

    @JsonProperty("warnings")
    private List<String> warnings;

    @JsonProperty("missingCapabilities")
    private List<String> missingCapabilities;

    public WorkflowPlanResponseDto() {}

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public List<PlannedTaskDto> getTaskList() { return taskList; }
    public void setTaskList(List<PlannedTaskDto> taskList) { this.taskList = taskList; }
    public Map<Integer, List<PlannedTaskDto>> getExecutionStages() { return executionStages; }
    public void setExecutionStages(Map<Integer, List<PlannedTaskDto>> executionStages) { this.executionStages = executionStages; }
    public List<List<String>> getParallelGroups() { return parallelGroups; }
    public void setParallelGroups(List<List<String>> parallelGroups) { this.parallelGroups = parallelGroups; }
    public Integer getTotalEstimatedDurationSeconds() { return totalEstimatedDurationSeconds; }
    public void setTotalEstimatedDurationSeconds(Integer totalEstimatedDurationSeconds) { this.totalEstimatedDurationSeconds = totalEstimatedDurationSeconds; }
    public Double getTotalEstimatedCost() { return totalEstimatedCost; }
    public void setTotalEstimatedCost(Double totalEstimatedCost) { this.totalEstimatedCost = totalEstimatedCost; }
    public List<String> getRequiredCapabilities() { return requiredCapabilities; }
    public void setRequiredCapabilities(List<String> requiredCapabilities) { this.requiredCapabilities = requiredCapabilities; }
    public Map<String, Object> getGraphRepresentation() { return graphRepresentation; }
    public void setGraphRepresentation(Map<String, Object> graphRepresentation) { this.graphRepresentation = graphRepresentation; }
    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    public List<String> getMissingCapabilities() { return missingCapabilities; }
    public void setMissingCapabilities(List<String> missingCapabilities) { this.missingCapabilities = missingCapabilities; }
}
