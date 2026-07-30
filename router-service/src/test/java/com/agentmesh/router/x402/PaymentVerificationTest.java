package com.agentmesh.router.x402;

import com.agentmesh.router.x402.dto.X402Challenge;
import com.agentmesh.router.x402.dto.X402PaymentProof;
import com.agentmesh.router.x402.provider.AlgorandX402Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PaymentVerificationTest {

    private AlgorandX402Provider provider;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        provider = new AlgorandX402Provider(
                "D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ",
                "https://facilitator.goplausible.xyz",
                "31566704",
                restTemplate
        );
    }

    @Test
    void testFacilitatorVerificationSuccess() {
        X402Challenge challenge = provider.generateChallenge("wf-fac", 10.0);
        X402PaymentProof proof = new X402PaymentProof(challenge.getChallengeId(), "TX99998888", "SENDER123", 10.0, "sig999");

        when(restTemplate.postForObject(eq("https://facilitator.goplausible.xyz/verify"), any(), eq(java.util.Map.class)))
                .thenReturn(java.util.Map.of("verified", true, "transactionId", "TX99998888"));

        boolean verified = provider.verifyPayment(proof, challenge);

        assertTrue(verified);
    }
}
