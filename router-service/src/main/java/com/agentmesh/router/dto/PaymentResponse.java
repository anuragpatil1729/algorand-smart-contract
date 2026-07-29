package com.agentmesh.router.dto;

import com.agentmesh.router.model.enums.PaymentStatus;
import java.time.LocalDateTime;

public class PaymentResponse {
    private String id;
    private String workflowId;
    private String escrowAddress;
    private Double totalAmount;
    private PaymentStatus paymentStatus;
    private String transactionHash;
    private String txGroupId;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public PaymentResponse() {}

    public PaymentResponse(String id, String workflowId, String escrowAddress, Double totalAmount, PaymentStatus paymentStatus, String transactionHash, String txGroupId, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.workflowId = workflowId;
        this.escrowAddress = escrowAddress;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.transactionHash = transactionHash;
        this.txGroupId = txGroupId;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public String getEscrowAddress() { return escrowAddress; }
    public void setEscrowAddress(String escrowAddress) { this.escrowAddress = escrowAddress; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getTransactionHash() { return transactionHash; }
    public void setTransactionHash(String transactionHash) { this.transactionHash = transactionHash; }
    public String getTxGroupId() { return txGroupId; }
    public void setTxGroupId(String txGroupId) { this.txGroupId = txGroupId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
