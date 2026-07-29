package com.agentmesh.router.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String id;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "tx_hash", nullable = false)
    private String txHash;

    @Column(name = "sender_wallet", nullable = false)
    private String senderWallet;

    @Column(name = "receiver_wallet", nullable = false)
    private String receiverWallet;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "agent_id")
    private String agentId;

    @Column(nullable = false)
    private String status;

    @Column(name = "block_round")
    private Long blockRound;

    private LocalDateTime timestamp;

    public Transaction() {}

    public Transaction(String id, String paymentId, String txHash, String senderWallet, String receiverWallet, Double amount, String agentId, String status, Long blockRound, LocalDateTime timestamp) {
        this.id = id;
        this.paymentId = paymentId;
        this.txHash = txHash;
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.amount = amount;
        this.agentId = agentId;
        this.status = status;
        this.blockRound = blockRound;
        this.timestamp = timestamp;
    }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String paymentId;
        private String txHash;
        private String senderWallet;
        private String receiverWallet;
        private Double amount;
        private String agentId;
        private String status;
        private Long blockRound;
        private LocalDateTime timestamp;

        public Builder id(String id) { this.id = id; return this; }
        public Builder paymentId(String paymentId) { this.paymentId = paymentId; return this; }
        public Builder txHash(String txHash) { this.txHash = txHash; return this; }
        public Builder senderWallet(String senderWallet) { this.senderWallet = senderWallet; return this; }
        public Builder receiverWallet(String receiverWallet) { this.receiverWallet = receiverWallet; return this; }
        public Builder amount(Double amount) { this.amount = amount; return this; }
        public Builder agentId(String agentId) { this.agentId = agentId; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder blockRound(Long blockRound) { this.blockRound = blockRound; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public Transaction build() {
            return new Transaction(id, paymentId, txHash, senderWallet, receiverWallet, amount, agentId, status, blockRound, timestamp);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getTxHash() { return txHash; }
    public void setTxHash(String txHash) { this.txHash = txHash; }
    public String getSenderWallet() { return senderWallet; }
    public void setSenderWallet(String senderWallet) { this.senderWallet = senderWallet; return; }
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
