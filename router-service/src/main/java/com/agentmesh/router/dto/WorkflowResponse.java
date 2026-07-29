package com.agentmesh.router.dto;

import java.time.LocalDateTime;
import java.util.List;

public class WorkflowResponse {
    private String id;
    private String prompt;
    private String status;
    private Double totalPrice;
    private String escrowAddress;
    private String escrowStatus;
    private String aggregatedResult;
    private List<TaskDto> tasks;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public WorkflowResponse() {}

    public WorkflowResponse(String id, String prompt, String status, Double totalPrice, String escrowAddress, String escrowStatus, String aggregatedResult, List<TaskDto> tasks, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.prompt = prompt;
        this.status = status;
        this.totalPrice = totalPrice;
        this.escrowAddress = escrowAddress;
        this.escrowStatus = escrowStatus;
        this.aggregatedResult = aggregatedResult;
        this.tasks = tasks;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String prompt;
        private String status;
        private Double totalPrice;
        private String escrowAddress;
        private String escrowStatus;
        private String aggregatedResult;
        private List<TaskDto> tasks;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder prompt(String prompt) { this.prompt = prompt; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder totalPrice(Double totalPrice) { this.totalPrice = totalPrice; return this; }
        public Builder escrowAddress(String escrowAddress) { this.escrowAddress = escrowAddress; return this; }
        public Builder escrowStatus(String escrowStatus) { this.escrowStatus = escrowStatus; return this; }
        public Builder aggregatedResult(String aggregatedResult) { this.aggregatedResult = aggregatedResult; return this; }
        public Builder tasks(List<TaskDto> tasks) { this.tasks = tasks; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public WorkflowResponse build() {
            return new WorkflowResponse(id, prompt, status, totalPrice, escrowAddress, escrowStatus, aggregatedResult, tasks, createdAt, completedAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public String getEscrowAddress() { return escrowAddress; }
    public void setEscrowAddress(String escrowAddress) { this.escrowAddress = escrowAddress; }
    public String getEscrowStatus() { return escrowStatus; }
    public void setEscrowStatus(String escrowStatus) { this.escrowStatus = escrowStatus; }
    public String getAggregatedResult() { return aggregatedResult; }
    public void setAggregatedResult(String aggregatedResult) { this.aggregatedResult = aggregatedResult; }
    public List<TaskDto> getTasks() { return tasks; }
    public void setTasks(List<TaskDto> tasks) { this.tasks = tasks; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
