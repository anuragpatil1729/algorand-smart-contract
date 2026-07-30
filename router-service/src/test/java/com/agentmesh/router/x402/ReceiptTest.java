package com.agentmesh.router.x402;

import com.agentmesh.router.x402.dto.X402Receipt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    @Test
    void testReceiptStructureAndHash() {
        X402Receipt receipt = new X402Receipt("wf-test-01", "exec-01", "tx-algo-12345678", "5.25", 5.25, "a1b2c3d4e5f678901234567890abcdef");

        assertEquals("wf-test-01", receipt.getWorkflowId());
        assertEquals("exec-01", receipt.getExecutionId());
        assertEquals("tx-algo-12345678", receipt.getAlgorandTransactionId());
        assertEquals("USDC", receipt.getAsset());
        assertEquals("5.25", receipt.getAmount());
        assertEquals(5.25, receipt.getWorkflowCost());
        assertTrue(receipt.getVerified());
        assertEquals("SETTLED", receipt.getPaymentStatus());
        assertNotNull(receipt.getReceipt());
    }
}
