package com.agentmesh.router.x402;

import com.agentmesh.router.x402.dto.X402Challenge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeTest {

    @Test
    void testChallengeCreationAndDefaults() {
        X402Challenge challenge = new X402Challenge("wf-100", 5.25, "D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ");

        assertNotNull(challenge.getChallengeId());
        assertEquals("wf-100", challenge.getWorkflowId());
        assertEquals(5.25, challenge.getPrice());
        assertEquals("USDC", challenge.getAsset());
        assertEquals("31566704", challenge.getAssetId());
        assertEquals("algorand-testnet", challenge.getNetwork());
        assertEquals("https://facilitator.goplausible.xyz", challenge.getFacilitatorUrl());
        assertTrue(challenge.getExpiresAt() > System.currentTimeMillis());
    }
}
