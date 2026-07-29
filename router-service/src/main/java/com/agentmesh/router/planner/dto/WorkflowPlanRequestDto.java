package com.agentmesh.router.planner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkflowPlanRequestDto {

    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("budget")
    private Double budget;

    @JsonProperty("deadlineSeconds")
    private Integer deadlineSeconds;

    @JsonProperty("priority")
    private String priority = "MEDIUM";

    public WorkflowPlanRequestDto() {}

    public WorkflowPlanRequestDto(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }
    public Integer getDeadlineSeconds() { return deadlineSeconds; }
    public void setDeadlineSeconds(Integer deadlineSeconds) { this.deadlineSeconds = deadlineSeconds; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
