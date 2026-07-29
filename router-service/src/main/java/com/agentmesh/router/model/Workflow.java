package com.agentmesh.router.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workflows")
public class Workflow {

    @Id
    private String id;

    @Column(nullable = false, length = 2000)
    private String prompt;

    @Column(nullable = false)
    private String status;

    @Column(name = "total_price")
    private Double totalPrice = 0.0;

    @Column(name = "escrow_address")
    private String escrowAddress;

    @Column(name = "escrow_status")
    private String escrowStatus = "NOT_CREATED";

    @Column(name = "aggregated_result", length = 10000)
    private String aggregatedResult;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Workflow() {}

    public Workflow(String id, String prompt, String status, Double totalPrice, String escrowAddress, String escrowStatus, String aggregatedResult, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.prompt = prompt;
        this.status = status;
        this.totalPrice = totalPrice != null ? totalPrice : 0.0;
        this.escrowAddress = escrowAddress;
        this.escrowStatus = escrowStatus != null ? escrowStatus : "NOT_CREATED";
        this.aggregatedResult = aggregatedResult;
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
        private String status;
        private Double totalPrice = 0.0;
        private String escrowAddress;
        private String escrowStatus = "NOT_CREATED";
        private String aggregatedResult;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder prompt(String prompt) { this.prompt = prompt; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder totalPrice(Double totalPrice) { this.totalPrice = totalPrice; return this; }
        public Builder escrowAddress(String escrowAddress) { this.escrowAddress = escrowAddress; return this; }
        public Builder escrowStatus(String escrowStatus) { this.escrowStatus = escrowStatus; return this; }
        public Builder aggregatedResult(String aggregatedResult) { this.aggregatedResult = aggregatedResult; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public Workflow build() {
            return new Workflow(id, prompt, status, totalPrice, escrowAddress, escrowStatus, aggregatedResult, createdAt, completedAt);
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
