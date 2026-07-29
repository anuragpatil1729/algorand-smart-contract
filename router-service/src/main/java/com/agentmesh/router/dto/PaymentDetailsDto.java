package com.agentmesh.router.dto;

import java.util.List;

public class PaymentDetailsDto {
    private String id;
    private String workflowId;
    private String escrowWallet;
    private Double totalAmount;
    private String status;
    private String txGroupId;
    private String createdAt;
    private String completedAt;
    private List<TransactionDto> transactions;

    public PaymentDetailsDto() {}

    public PaymentDetailsDto(String id, String workflowId, String escrowWallet, Double totalAmount, String status, String txGroupId, String createdAt, String completedAt, List<TransactionDto> transactions) {
        this.id = id;
        this.workflowId = workflowId;
        this.escrowWallet = escrowWallet;
        this.totalAmount = totalAmount;
        this.status = status;
        this.txGroupId = txGroupId;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.transactions = transactions;
    }

    public static class TransactionDto {
        private String txHash;
        private String senderWallet;
        private String receiverWallet;
        private Double amount;
        private String agentId;
        private String status;
        private Long blockRound;
        private String timestamp;

        public TransactionDto() {}

        public TransactionDto(String txHash, String senderWallet, String receiverWallet, Double amount, String agentId, String status, Long blockRound, String timestamp) {
            this.txHash = txHash;
            this.senderWallet = senderWallet;
            this.receiverWallet = receiverWallet;
            this.amount = amount;
            this.agentId = agentId;
            this.status = status;
            this.blockRound = blockRound;
            this.timestamp = timestamp;
        }

        public String getTxHash() { return txHash; }
        public void setTxHash(String txHash) { this.txHash = txHash; }
        public String getSenderWallet() { return senderWallet; }
        public void setSenderWallet(String senderWallet) { this.senderWallet = senderWallet; }
        public String getReceiverWallet() { return receiverWallet; }
        public void setReceiverWallet(String receiverWallet) { this.receiverWallet = receiverWallet; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Long getBlockRound() { return blockRound; }
        public void setBlockRound(Long blockRound) { this.blockRound = blockRound; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
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
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getCompletedAt() { return completedAt; }
    public void setCompletedAt(String completedAt) { this.completedAt = completedAt; }
    public List<TransactionDto> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionDto> transactions) { this.transactions = transactions; }
}
