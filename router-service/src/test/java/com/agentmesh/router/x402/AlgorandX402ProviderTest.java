package com.agentmesh.router.x402;

import com.agentmesh.router.x402.dto.X402Challenge;
import com.agentmesh.router.x402.dto.X402PaymentProof;
import com.agentmesh.router.x402.dto.X402Receipt;
import com.agentmesh.router.x402.model.PaymentContext;
import com.agentmesh.router.x402.provider.AlgorandX402Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlgorandX402ProviderTest {

    private AlgorandX402Provider provider;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        provider = new AlgorandX402Provider(
                "D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ",
                "https://facilitator.goplausible.xyz",
                "31566704",
                restTemplate
        );
    }

    @Test
    void testGenerateChallenge() {
        X402Challenge challenge = provider.generateChallenge("wf-500", 7.50);

        assertNotNull(challenge);
        assertEquals("wf-500", challenge.getWorkflowId());
        assertEquals(7.50, challenge.getPrice());
        assertEquals("algorand-testnet", challenge.getNetwork());
        assertEquals("31566704", challenge.getAssetId());
    }

    @Test
    void testVerifyPaymentProof() {
        X402Challenge challenge = provider.generateChallenge("wf-500", 7.50);
        X402PaymentProof proof = new X402PaymentProof(challenge.getChallengeId(), "TX1234567890", "SENDERADDR", 7.50, "sig-123");

        boolean verified = provider.verifyPayment(proof, challenge);

        assertTrue(verified);
    }

    @Test
    void testGenerateReceipt() {
        PaymentContext context = new PaymentContext();
        context.setWorkflowId("wf-777");
        context.setExecutionId("exec-777");
        context.setTransactionId("TX-ALGO-TEST-777");
        context.setAmountPaid(6.00);

        X402Receipt receipt = provider.generateReceipt(context);

        assertNotNull(receipt);
        assertEquals("wf-777", receipt.getWorkflowId());
        assertEquals("TX-ALGO-TEST-777", receipt.getAlgorandTransactionId());
        assertTrue(receipt.getVerified());
        assertNotNull(receipt.getReceiptHash());
    }
}
