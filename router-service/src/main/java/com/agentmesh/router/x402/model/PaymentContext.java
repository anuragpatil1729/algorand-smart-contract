package com.agentmesh.router.x402.model;

import com.agentmesh.router.x402.dto.X402Challenge;
import com.agentmesh.router.x402.dto.X402PaymentProof;
import com.agentmesh.router.x402.dto.X402Receipt;

import java.util.HashMap;
import java.util.Map;

public class PaymentContext {

    private static final ThreadLocal<PaymentContext> CURRENT_CONTEXT = new ThreadLocal<>();

    private String workflowId;
    private String executionId;
    private String challengeId;
    private String paymentStatus = "UNPAID";
    private Boolean verified = false;
    private String transactionId;
    private Double amountPaid = 0.0;
    private X402Challenge challenge;
    private X402PaymentProof paymentProof;
    private X402Receipt receipt;
    private Map<String, Object> settlementMetadata = new HashMap<>();

    public PaymentContext() {}

    public static PaymentContext getCurrent() {
        PaymentContext context = CURRENT_CONTEXT.get();
        if (context == null) {
            context = new PaymentContext();
            CURRENT_CONTEXT.set(context);
        }
        return context;
    }

    public static void setCurrent(PaymentContext context) {
        CURRENT_CONTEXT.set(context);
    }

    public static void clear() {
        CURRENT_CONTEXT.remove();
    }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }

    public String getChallengeId() { return challengeId; }
    public void setChallengeId(String challengeId) { this.challengeId = challengeId; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public X402Challenge getChallenge() { return challenge; }
    public void setChallenge(X402Challenge challenge) { this.challenge = challenge; }

    public X402PaymentProof getPaymentProof() { return paymentProof; }
    public void setPaymentProof(X402PaymentProof paymentProof) { this.paymentProof = paymentProof; }

    public X402Receipt getReceipt() { return receipt; }
    public void setReceipt(X402Receipt receipt) { this.receipt = receipt; }

    public Map<String, Object> getSettlementMetadata() { return settlementMetadata; }
    public void setSettlementMetadata(Map<String, Object> settlementMetadata) { this.settlementMetadata = settlementMetadata; }
}
