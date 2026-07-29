package com.agentmesh.router.model;

import com.agentmesh.router.model.enums.WorkflowStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workflows")
public class Workflow {

    @Id
    private String id;

    @Column(nullable = false, length = 2000)
    private String prompt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowStatus status = WorkflowStatus.PENDING_APPROVAL;

    @Column(name = "total_cost")
    private Double totalCost = 0.0;

    @Column(name = "escrow_address")
    private String escrowAddress;

    @Column(name = "escrow_status")
    private String escrowStatus = "NOT_CREATED";

    @Column(name = "aggregated_result", length = 10000)
    private String aggregatedResult;

    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Workflow() {}

    public Workflow(String id, String prompt, WorkflowStatus status, Double totalCost, String escrowAddress, String escrowStatus, String aggregatedResult, List<Task> tasks, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.prompt = prompt;
        this.status = status != null ? status : WorkflowStatus.PENDING_APPROVAL;
        this.totalCost = totalCost != null ? totalCost : 0.0;
        this.escrowAddress = escrowAddress;
        this.escrowStatus = escrowStatus != null ? escrowStatus : "NOT_CREATED";
        this.aggregatedResult = aggregatedResult;
        if (tasks != null) this.tasks = tasks;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String prompt;
        private WorkflowStatus status = WorkflowStatus.PENDING_APPROVAL;
        private Double totalCost = 0.0;
        private String escrowAddress;
        private String escrowStatus = "NOT_CREATED";
        private String aggregatedResult;
        private List<Task> tasks = new ArrayList<>();
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder prompt(String prompt) { this.prompt = prompt; return this; }
        public Builder status(WorkflowStatus status) { this.status = status; return this; }
        public Builder status(String statusStr) {
            if (statusStr != null) {
                try {
                    this.status = WorkflowStatus.valueOf(statusStr.toUpperCase());
                } catch (Exception e) {
                    this.status = WorkflowStatus.PENDING_APPROVAL;
                }
            }
            return this;
        }

        public Builder totalCost(Double totalCost) { this.totalCost = totalCost; return this; }
        public Builder totalPrice(Double totalPrice) { this.totalCost = totalPrice; return this; }

        public Builder escrowAddress(String escrowAddress) { this.escrowAddress = escrowAddress; return this; }
        public Builder escrowStatus(String escrowStatus) { this.escrowStatus = escrowStatus; return this; }
        public Builder aggregatedResult(String aggregatedResult) { this.aggregatedResult = aggregatedResult; return this; }
        public Builder tasks(List<Task> tasks) { this.tasks = tasks; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public Workflow build() {
            return new Workflow(id, prompt, status, totalCost, escrowAddress, escrowStatus, aggregatedResult, tasks, createdAt, completedAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public WorkflowStatus getStatusEnum() { return status; }
    public String getStatus() { return status != null ? status.name() : "PENDING_APPROVAL"; }
    public void setStatus(WorkflowStatus status) { this.status = status; }
    public void setStatus(String statusStr) {
        if (statusStr != null) {
            try {
                this.status = WorkflowStatus.valueOf(statusStr.toUpperCase());
            } catch (Exception e) {
                this.status = WorkflowStatus.PENDING_APPROVAL;
            }
        }
    }

    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }

    public Double getTotalPrice() { return totalCost; }
    public void setTotalPrice(Double totalPrice) { this.totalCost = totalPrice; }

    public String getEscrowAddress() { return escrowAddress; }
    public void setEscrowAddress(String escrowAddress) { this.escrowAddress = escrowAddress; }
    public String getEscrowStatus() { return escrowStatus; }
    public void setEscrowStatus(String escrowStatus) { this.escrowStatus = escrowStatus; }
    public String getAggregatedResult() { return aggregatedResult; }
    public void setAggregatedResult(String aggregatedResult) { this.aggregatedResult = aggregatedResult; }
    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
