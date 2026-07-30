package com.agentmesh.router.execution.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class ExecutionTaskRequest {

    @JsonProperty("taskId")
    @JsonAlias({"taskId", "task_id"})
    private String taskId;

    @JsonProperty("workflowId")
    @JsonAlias({"workflowId", "workflow_id"})
    private String workflowId;

    @JsonProperty("taskType")
    @JsonAlias({"taskType", "task_type"})
    private String taskType;

    @JsonProperty("description")
    private String description;

    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("context")
    private Map<String, Object> context;

    @JsonProperty("dependenciesOutput")
    @JsonAlias({"dependenciesOutput", "dependencies_output"})
    private Map<String, Object> dependenciesOutput;

    @JsonProperty("budget")
    private Double budget;

    @JsonProperty("deadline")
    private String deadline;

    public ExecutionTaskRequest() {}

    public ExecutionTaskRequest(String taskId, String workflowId, String taskType, String description,
                                String prompt, Map<String, Object> context, Map<String, Object> dependenciesOutput,
                                Double budget, String deadline) {
        this.taskId = taskId;
        this.workflowId = workflowId;
        this.taskType = taskType;
        this.description = description;
        this.prompt = prompt;
        this.context = context;
        this.dependenciesOutput = dependenciesOutput;
        this.budget = budget;
        this.deadline = deadline;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }

    public Map<String, Object> getDependenciesOutput() { return dependenciesOutput; }
    public void setDependenciesOutput(Map<String, Object> dependenciesOutput) { this.dependenciesOutput = dependenciesOutput; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
}
