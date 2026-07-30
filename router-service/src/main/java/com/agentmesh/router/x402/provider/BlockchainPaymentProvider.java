package com.agentmesh.router.x402.provider;

import com.agentmesh.router.x402.dto.X402Challenge;
import com.agentmesh.router.x402.dto.X402PaymentProof;
import com.agentmesh.router.x402.dto.X402Receipt;
import com.agentmesh.router.x402.model.PaymentContext;

public interface BlockchainPaymentProvider {

    String getNetworkName();

    X402Challenge generateChallenge(String workflowId, double amount);

    boolean verifyPayment(X402PaymentProof proof, X402Challenge challenge);

    X402Receipt generateReceipt(PaymentContext context);
}
