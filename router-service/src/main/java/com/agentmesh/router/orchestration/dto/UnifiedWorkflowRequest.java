package com.agentmesh.router.orchestration.dto;

import com.agentmesh.router.x402.dto.X402PaymentProof;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnifiedWorkflowRequest {

    @JsonProperty("prompt")
    private String prompt;

    @JsonProperty("strategy")
    private String strategy = "BALANCED";

    @JsonProperty("maxConcurrency")
    private Integer maxConcurrency = 5;

    @JsonProperty("paymentProof")
    private X402PaymentProof paymentProof;

    public UnifiedWorkflowRequest() {}

    public UnifiedWorkflowRequest(String prompt) {
        this.prompt = prompt;
    }

    public UnifiedWorkflowRequest(String prompt, String strategy) {
        this.prompt = prompt;
        this.strategy = strategy != null ? strategy : "BALANCED";
    }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

    public Integer getMaxConcurrency() { return maxConcurrency; }
    public void setMaxConcurrency(Integer maxConcurrency) { this.maxConcurrency = maxConcurrency; }

    public X402PaymentProof getPaymentProof() { return paymentProof; }
    public void setPaymentProof(X402PaymentProof paymentProof) { this.paymentProof = paymentProof; }
}
