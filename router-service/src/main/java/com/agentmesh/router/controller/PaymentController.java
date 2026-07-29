package com.agentmesh.router.controller;

import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.dto.PaymentResponse;
import com.agentmesh.router.model.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment Controller", description = "Endpoints for Algorand Escrow locking, payouts, and history")
public class PaymentController {

    @GetMapping("/workflow/{workflowId}")
    @Operation(summary = "Get Algorand payment details for a specific workflow")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByWorkflowId(@PathVariable String workflowId) {
        PaymentResponse placeholder = new PaymentResponse(
                "pay-" + UUID.randomUUID().toString().substring(0, 8),
                workflowId,
                "6SA3SIOK5ZE3VU3K3CJXUOOOQ2NKDHVPLOUCO5KIGZPS32JL7SGA6ZAY6Y",
                150.0,
                PaymentStatus.HELD_IN_ESCROW,
                "ALG-TX-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase(),
                "ALG-GROUP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.ok(ApiResponse.success(placeholder));
    }

    @GetMapping
    @Operation(summary = "List all Algorand payment transactions")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", new ArrayList<>()));
    }
}
