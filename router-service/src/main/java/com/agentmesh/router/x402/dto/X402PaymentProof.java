package com.agentmesh.router.x402.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class X402PaymentProof {

    @JsonProperty("challengeId")
    @JsonAlias({"challengeId", "challenge_id"})
    private String challengeId;

    @JsonProperty("transactionId")
    @JsonAlias({"transactionId", "transaction_id", "txId", "tx_id"})
    private String transactionId;

    @JsonProperty("senderAddress")
    @JsonAlias({"senderAddress", "sender_address", "sender"})
    private String senderAddress;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("asset")
    private String asset = "USDC";

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("proofPayload")
    @JsonAlias({"proofPayload", "proof_payload"})
    private String proofPayload;

    @JsonProperty("timestamp")
    private Long timestamp = System.currentTimeMillis();

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    public X402PaymentProof() {}

    public X402PaymentProof(String challengeId, String transactionId, String senderAddress, Double amount, String signature) {
        this.challengeId = challengeId;
        this.transactionId = transactionId;
        this.senderAddress = senderAddress;
        this.amount = amount;
        this.signature = signature;
    }

    public String getChallengeId() { return challengeId; }
    public void setChallengeId(String challengeId) { this.challengeId = challengeId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getSenderAddress() { return senderAddress; }
    public void setSenderAddress(String senderAddress) { this.senderAddress = senderAddress; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getAsset() { return asset; }
    public void setAsset(String asset) { this.asset = asset; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public String getProofPayload() { return proofPayload; }
    public void setProofPayload(String proofPayload) { this.proofPayload = proofPayload; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
