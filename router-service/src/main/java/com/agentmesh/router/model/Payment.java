package com.agentmesh.router.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    private String id;

    @Column(name = "workflow_id", nullable = false)
    private String workflowId;

    @Column(name = "escrow_wallet", nullable = false)
    private String escrowWallet;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String status;

    @Column(name = "tx_group_id")
    private String txGroupId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Payment() {}

    public Payment(String id, String workflowId, String escrowWallet, Double totalAmount, String status, String txGroupId, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.workflowId = workflowId;
        this.escrowWallet = escrowWallet;
        this.totalAmount = totalAmount;
        this.status = status;
        this.txGroupId = txGroupId;
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
        private String workflowId;
        private String escrowWallet;
        private Double totalAmount;
        private String status;
        private String txGroupId;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder workflowId(String workflowId) { this.workflowId = workflowId; return this; }
        public Builder escrowWallet(String escrowWallet) { this.escrowWallet = escrowWallet; return this; }
        public Builder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder txGroupId(String txGroupId) { this.txGroupId = txGroupId; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public Payment build() {
            return new Payment(id, workflowId, escrowWallet, totalAmount, status, txGroupId, createdAt, completedAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public String getEscrowWallet() { return escrowWallet; }
    public void setEscrowWallet(String escrowWallet) { this.escrowWallet = escrowWallet; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTxGroupId() { return txGroupId; }
    public void setTxGroupId(String txGroupId) { this.txGroupId = txGroupId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
