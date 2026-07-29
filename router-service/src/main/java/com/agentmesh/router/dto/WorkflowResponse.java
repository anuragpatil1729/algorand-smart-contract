package com.agentmesh.router.dto;

import com.agentmesh.router.model.enums.WorkflowStatus;
import java.time.LocalDateTime;
import java.util.List;

public class WorkflowResponse {
    private String id;
    private String prompt;
    private WorkflowStatus status;
    private Double totalCost;
    private String escrowAddress;
    private String escrowStatus;
    private String aggregatedResult;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private List<TaskResponse> tasks;

    public WorkflowResponse() {}

    public WorkflowResponse(String id, String prompt, WorkflowStatus status, Double totalCost, LocalDateTime createdAt, LocalDateTime completedAt, List<TaskResponse> tasks) {
        this.id = id;
        this.prompt = prompt;
        this.status = status;
        this.totalCost = totalCost;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.tasks = tasks;
    }

    public static class TaskResponse {
        private String id;
        private String taskType;
        private String description;
        private String assignedAgentId;
        private String status;
        private Double price;
        private String dependency;
        private Long executionTime;
        private String output;

        public TaskResponse() {}

        public TaskResponse(String id, String taskType, String description, String assignedAgentId, String status, Double price, String dependency, Long executionTime, String output) {
            this.id = id;
            this.taskType = taskType;
            this.description = description;
            this.assignedAgentId = assignedAgentId;
            this.status = status;
            this.price = price;
            this.dependency = dependency;
            this.executionTime = executionTime;
            this.output = output;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTaskType() { return taskType; }
        public void setTaskType(String taskType) { this.taskType = taskType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getAssignedAgentId() { return assignedAgentId; }
        public void setAssignedAgentId(String assignedAgentId) { this.assignedAgentId = assignedAgentId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public String getDependency() { return dependency; }
        public void setDependency(String dependency) { this.dependency = dependency; }
        public Long getExecutionTime() { return executionTime; }
        public void setExecutionTime(Long executionTime) { this.executionTime = executionTime; }
        public String getOutput() { return output; }
        public void setOutput(String output) { this.output = output; }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public WorkflowStatus getStatus() { return status; }
    public void setStatus(WorkflowStatus status) { this.status = status; }
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
    public String getEscrowAddress() { return escrowAddress; }
    public void setEscrowAddress(String escrowAddress) { this.escrowAddress = escrowAddress; }
    public String getEscrowStatus() { return escrowStatus; }
    public void setEscrowStatus(String escrowStatus) { this.escrowStatus = escrowStatus; }
    public String getAggregatedResult() { return aggregatedResult; }
    public void setAggregatedResult(String aggregatedResult) { this.aggregatedResult = aggregatedResult; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public List<TaskResponse> getTasks() { return tasks; }
    public void setTasks(List<TaskResponse> tasks) { this.tasks = tasks; }
}
