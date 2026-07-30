package com.agentmesh.router.x402;

import com.agentmesh.router.x402.dto.X402Challenge;
import com.agentmesh.router.x402.dto.X402PaymentProof;
import com.agentmesh.router.x402.provider.AlgorandX402Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MockFacilitatorTest {

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
    void testFacilitatorOfflineFallbackVerification() {
        X402Challenge challenge = provider.generateChallenge("wf-mock", 4.0);
        X402PaymentProof proof = new X402PaymentProof(challenge.getChallengeId(), "TX-FALLBACK-001", "SENDER", 4.0, "sig-mock");

        when(restTemplate.postForObject(anyString(), any(), eq(java.util.Map.class)))
                .thenThrow(new RestClientException("Facilitator connection timeout"));

        boolean verified = provider.verifyPayment(proof, challenge);

        // Fallback validation should verify non-empty txId & positive amount
        assertTrue(verified);
    }
}
