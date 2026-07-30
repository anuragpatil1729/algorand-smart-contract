package com.agentmesh.router.quote.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class AgentQuoteRequest {

    @JsonProperty("taskId")
    @JsonAlias({"taskId", "task_id"})
    private String taskId;

    @JsonProperty("workflowId")
    @JsonAlias({"workflowId", "workflow_id"})
    private String workflowId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("taskType")
    @JsonAlias({"taskType", "task_type", "task"})
    private String taskType;

    @JsonProperty("requiredCapability")
    @JsonAlias({"requiredCapability", "required_capability", "capability"})
    private String requiredCapability;

    @JsonProperty("priority")
    private String priority = "MEDIUM";

    @JsonProperty("estimatedComplexity")
    @JsonAlias({"estimatedComplexity", "estimated_complexity"})
    private String estimatedComplexity = "MEDIUM";

    @JsonProperty("budget")
    private Double budget;

    @JsonProperty("deadline")
    private String deadline;

    @JsonProperty("context")
    private Map<String, Object> context;

    public AgentQuoteRequest() {}

    public AgentQuoteRequest(String taskId, String workflowId, String description, String taskType,
                             String requiredCapability, String priority, String estimatedComplexity,
                             Double budget, String deadline, Map<String, Object> context) {
        this.taskId = taskId;
        this.workflowId = workflowId;
        this.description = description;
        this.taskType = taskType;
        this.requiredCapability = requiredCapability;
        this.priority = priority != null ? priority : "MEDIUM";
        this.estimatedComplexity = estimatedComplexity != null ? estimatedComplexity : "MEDIUM";
        this.budget = budget;
        this.deadline = deadline;
        this.context = context;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getRequiredCapability() { return requiredCapability; }
    public void setRequiredCapability(String requiredCapability) { this.requiredCapability = requiredCapability; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getEstimatedComplexity() { return estimatedComplexity; }
    public void setEstimatedComplexity(String estimatedComplexity) { this.estimatedComplexity = estimatedComplexity; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }
}
