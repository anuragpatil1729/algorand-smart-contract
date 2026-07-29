package com.agentmesh.router.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PaymentDetailsDto {
    private String id;
    private String workflowId;
    private String escrowWallet;
    private Double totalAmount;
    private String status;
    private String txGroupId;
    private List<TransactionDto> transactions;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public PaymentDetailsDto() {}

    public PaymentDetailsDto(String id, String workflowId, String escrowWallet, Double totalAmount, String status, String txGroupId, List<TransactionDto> transactions, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.workflowId = workflowId;
        this.escrowWallet = escrowWallet;
        this.totalAmount = totalAmount;
        this.status = status;
        this.txGroupId = txGroupId;
        this.transactions = transactions;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String workflowId;
        private String escrowWallet;
        private Double totalAmount;
        private String status;
        private String txGroupId;
        private List<TransactionDto> transactions;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder workflowId(String workflowId) { this.workflowId = workflowId; return this; }
        public Builder escrowWallet(String escrowWallet) { this.escrowWallet = escrowWallet; return this; }
        public Builder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder txGroupId(String txGroupId) { this.txGroupId = txGroupId; return this; }
        public Builder transactions(List<TransactionDto> transactions) { this.transactions = transactions; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public PaymentDetailsDto build() {
            return new PaymentDetailsDto(id, workflowId, escrowWallet, totalAmount, status, txGroupId, transactions, createdAt, completedAt);
        }
    }

    public static class TransactionDto {
        private String id;
        private String txHash;
        private String senderWallet;
        private String receiverWallet;
        private Double amount;
        private String agentId;
        private String status;
        private Long blockRound;
        private LocalDateTime timestamp;

        public TransactionDto() {}

        public TransactionDto(String id, String txHash, String senderWallet, String receiverWallet, Double amount, String agentId, String status, Long blockRound, LocalDateTime timestamp) {
            this.id = id;
            this.txHash = txHash;
            this.senderWallet = senderWallet;
            this.receiverWallet = receiverWallet;
            this.amount = amount;
            this.agentId = agentId;
            this.status = status;
            this.blockRound = blockRound;
            this.timestamp = timestamp;
        }

        public static TxBuilder builder() { return new TxBuilder(); }

        public static class TxBuilder {
            private String id;
            private String txHash;
            private String senderWallet;
            private String receiverWallet;
            private Double amount;
            private String agentId;
            private String status;
            private Long blockRound;
            private LocalDateTime timestamp;

            public TxBuilder id(String id) { this.id = id; return this; }
            public TxBuilder txHash(String txHash) { this.txHash = txHash; return this; }
            public TxBuilder senderWallet(String senderWallet) { this.senderWallet = senderWallet; return this; }
            public TxBuilder receiverWallet(String receiverWallet) { this.receiverWallet = receiverWallet; return this; }
            public TxBuilder amount(Double amount) { this.amount = amount; return this; }
            public TxBuilder agentId(String agentId) { this.agentId = agentId; return this; }
            public TxBuilder status(String status) { this.status = status; return this; }
            public TxBuilder blockRound(Long blockRound) { this.blockRound = blockRound; return this; }
            public TxBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

            public TransactionDto build() {
                return new TransactionDto(id, txHash, senderWallet, receiverWallet, amount, agentId, status, blockRound, timestamp);
            }
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
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
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
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
    public List<TransactionDto> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionDto> transactions) { this.transactions = transactions; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
