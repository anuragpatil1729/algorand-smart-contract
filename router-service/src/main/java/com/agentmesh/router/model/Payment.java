package com.agentmesh.router.model;

import com.agentmesh.router.model.enums.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @Column(name = "escrow_address", nullable = false)
    private String escrowAddress;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.HELD_IN_ESCROW;

    @Column(name = "transaction_hash")
    private String transactionHash;

    @Column(name = "tx_group_id")
    private String txGroupId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Payment() {}

    public Payment(String id, Workflow workflow, String escrowAddress, Double totalAmount, PaymentStatus paymentStatus, String transactionHash, String txGroupId, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.workflow = workflow;
        this.escrowAddress = escrowAddress;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus != null ? paymentStatus : PaymentStatus.HELD_IN_ESCROW;
        this.transactionHash = transactionHash;
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
        private Workflow workflow;
        private String escrowAddress;
        private Double totalAmount;
        private PaymentStatus paymentStatus = PaymentStatus.HELD_IN_ESCROW;
        private String transactionHash;
        private String txGroupId;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder workflow(Workflow workflow) { this.workflow = workflow; return this; }
        public Builder workflowId(String workflowId) {
            if (workflowId != null) this.workflow = Workflow.builder().id(workflowId).build();
            return this;
        }

        public Builder escrowAddress(String escrowAddress) { this.escrowAddress = escrowAddress; return this; }
        public Builder escrowWallet(String escrowWallet) { this.escrowAddress = escrowWallet; return this; }
        public Builder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public Builder status(String statusStr) {
            if (statusStr != null) {
                try {
                    this.paymentStatus = PaymentStatus.valueOf(statusStr.toUpperCase());
                } catch (Exception e) {
                    this.paymentStatus = PaymentStatus.HELD_IN_ESCROW;
                }
            }
            return this;
        }

        public Builder transactionHash(String transactionHash) { this.transactionHash = transactionHash; return this; }
        public Builder txGroupId(String txGroupId) { this.txGroupId = txGroupId; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public Payment build() {
            return new Payment(id, workflow, escrowAddress, totalAmount, paymentStatus, transactionHash, txGroupId, createdAt, completedAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Workflow getWorkflow() { return workflow; }
    public void setWorkflow(Workflow workflow) { this.workflow = workflow; }

    public String getWorkflowId() { return workflow != null ? workflow.getId() : null; }
    public void setWorkflowId(String workflowId) {
        if (workflowId != null) this.workflow = Workflow.builder().id(workflowId).build();
    }

    public String getEscrowAddress() { return escrowAddress; }
    public void setEscrowAddress(String escrowAddress) { this.escrowAddress = escrowAddress; }

    public String getEscrowWallet() { return escrowAddress; }
    public void setEscrowWallet(String escrowWallet) { this.escrowAddress = escrowWallet; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getStatus() { return paymentStatus != null ? paymentStatus.name() : "HELD_IN_ESCROW"; }
    public void setStatus(String status) {
        try {
            this.paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            this.paymentStatus = PaymentStatus.HELD_IN_ESCROW;
        }
    }

    public String getTransactionHash() { return transactionHash; }
    public void setTransactionHash(String transactionHash) { this.transactionHash = transactionHash; }
    public String getTxGroupId() { return txGroupId; }
    public void setTxGroupId(String txGroupId) { this.txGroupId = txGroupId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
