package com.agentmesh.router.controller;

import com.agentmesh.router.dto.PaymentDetailsDto;
import com.agentmesh.router.service.AgentMeshService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final AgentMeshService agentMeshService;

    public PaymentController(AgentMeshService agentMeshService) {
        this.agentMeshService = agentMeshService;
    }

    @GetMapping("/{workflowId}")
    public ResponseEntity<PaymentDetailsDto> getPaymentDetails(@PathVariable String workflowId) {
        PaymentDetailsDto payment = agentMeshService.getPaymentDetails(workflowId);
        return ResponseEntity.ok(payment);
    }
}
