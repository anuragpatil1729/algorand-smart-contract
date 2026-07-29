package com.agentmesh.router.dto;

import java.util.List;

public class WorkflowDto {
    private String id;
    private String prompt;
    private String status;
    private Double totalPrice;
    private String escrowAddress;
    private String escrowStatus;
    private String aggregatedResult;
    private List<TaskDto> tasks;

    public WorkflowDto() {}

    public WorkflowDto(String id, String prompt, String status, Double totalPrice, String escrowAddress, String escrowStatus, String aggregatedResult, List<TaskDto> tasks) {
        this.id = id;
        this.prompt = prompt;
        this.status = status;
        this.totalPrice = totalPrice;
        this.escrowAddress = escrowAddress;
        this.escrowStatus = escrowStatus;
        this.aggregatedResult = aggregatedResult;
        this.tasks = tasks;
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
}
