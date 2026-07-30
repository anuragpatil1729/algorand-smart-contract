package com.agentmesh.router.x402.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class X402Receipt {

    @JsonProperty("workflowId")
    private String workflowId;

    @JsonProperty("executionId")
    private String executionId;

    @JsonProperty("algorandTransactionId")
    private String algorandTransactionId;

    @JsonProperty("asset")
    private String asset = "USDC";

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("workflowCost")
    private Double workflowCost;

    @JsonProperty("receipt")
    private String receipt;

    @JsonProperty("receiptHash")
    private String receiptHash;

    @JsonProperty("facilitatorStatus")
    private String facilitatorStatus = "VERIFIED_BY_PLAUSIBLE_FACILITATOR";

    @JsonProperty("verified")
    private Boolean verified = true;

    @JsonProperty("settlementTimestamp")
    private Long settlementTimestamp = System.currentTimeMillis();

    @JsonProperty("paymentStatus")
    private String paymentStatus = "SETTLED";

    public X402Receipt() {}

    public X402Receipt(String workflowId, String executionId, String algorandTransactionId, String amount, Double workflowCost, String receiptHash) {
        this.workflowId = workflowId;
        this.executionId = executionId;
        this.algorandTransactionId = algorandTransactionId;
        this.amount = amount;
        this.workflowCost = workflowCost;
        this.receiptHash = receiptHash;
        this.receipt = "x402-rcpt-" + (receiptHash != null && receiptHash.length() >= 16 ? receiptHash.substring(0, 16) : System.currentTimeMillis());
    }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }

    public String getAlgorandTransactionId() { return algorandTransactionId; }
    public void setAlgorandTransactionId(String algorandTransactionId) { this.algorandTransactionId = algorandTransactionId; }

    public String getAsset() { return asset; }
    public void setAsset(String asset) { this.asset = asset; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public Double getWorkflowCost() { return workflowCost; }
    public void setWorkflowCost(Double workflowCost) { this.workflowCost = workflowCost; }

    public String getReceipt() { return receipt; }
    public void setReceipt(String receipt) { this.receipt = receipt; }

    public String getReceiptHash() { return receiptHash; }
    public void setReceiptHash(String receiptHash) { this.receiptHash = receiptHash; }

    public String getFacilitatorStatus() { return facilitatorStatus; }
    public void setFacilitatorStatus(String facilitatorStatus) { this.facilitatorStatus = facilitatorStatus; }

    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }

    public Long getSettlementTimestamp() { return settlementTimestamp; }
    public void setSettlementTimestamp(Long settlementTimestamp) { this.settlementTimestamp = settlementTimestamp; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
