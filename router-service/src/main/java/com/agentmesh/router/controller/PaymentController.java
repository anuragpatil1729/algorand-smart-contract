package com.agentmesh.router.controller;

import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.x402.dto.X402Receipt;
import com.agentmesh.router.x402.service.X402AuditService;
import com.agentmesh.router.x402.service.X402PaymentMetrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "x402 Payment Controller", description = "Endpoints for x402 payment receipts, transaction history, and Algorand Testnet settlement metrics")
public class PaymentController {

    private final X402AuditService auditService;
    private final X402PaymentMetrics paymentMetrics;

    public PaymentController(X402AuditService auditService, X402PaymentMetrics paymentMetrics) {
        this.auditService = auditService;
        this.paymentMetrics = paymentMetrics;
    }

    @GetMapping("/receipt/{workflowId}")
    @Operation(summary = "Retrieve transaction-linked x402 receipt for a workflow ID")
    public ResponseEntity<ApiResponse<X402Receipt>> getReceiptByWorkflow(@PathVariable String workflowId) {
        X402Receipt receipt = auditService.getReceiptByWorkflow(workflowId);
        if (receipt == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("Receipt not found for workflowId: " + workflowId));
        }
        return ResponseEntity.ok(ApiResponse.success("x402 receipt retrieved", receipt));
    }

    @GetMapping("/transaction/{transactionId}")
    @Operation(summary = "Retrieve payment receipt by Algorand transaction ID")
    public ResponseEntity<ApiResponse<X402Receipt>> getReceiptByTransaction(@PathVariable String transactionId) {
        X402Receipt receipt = auditService.getReceiptByTransaction(transactionId);
        if (receipt == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("Receipt not found for Algorand transactionId: " + transactionId));
        }
        return ResponseEntity.ok(ApiResponse.success("x402 transaction receipt retrieved", receipt));
    }

    @GetMapping("/history")
    @Operation(summary = "Retrieve history of all verified x402 settlements")
    public ResponseEntity<ApiResponse<List<X402Receipt>>> getPaymentHistory() {
        List<X402Receipt> history = auditService.getAllReceipts();
        return ResponseEntity.ok(ApiResponse.success("Payment settlement history retrieved", history));
    }

    @GetMapping("/metrics")
    @Operation(summary = "Retrieve x402 payment telemetry and revenue metrics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPaymentMetrics() {
        Map<String, Object> metrics = paymentMetrics.getMetrics();
        return ResponseEntity.ok(ApiResponse.success("x402 payment metrics retrieved", metrics));
    }
}
