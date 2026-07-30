package com.agentmesh.router.controller;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.execution.ExecutionMonitor;
import com.agentmesh.router.orchestration.WorkflowOrchestrator;
import com.agentmesh.router.orchestration.dto.*;
import com.agentmesh.router.x402.service.X402PaymentMetrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
@Tag(name = "Demo & System Controller", description = "One-click end-to-end demo execution pipeline, system component health, and global telemetry metrics")
public class DemoController {

    private final WorkflowOrchestrator orchestrator;
    private final AgentDiscoveryService discoveryService;
    private final ExecutionMonitor executionMonitor;
    private final X402PaymentMetrics paymentMetrics;

    public DemoController(
            WorkflowOrchestrator orchestrator,
            AgentDiscoveryService discoveryService,
            ExecutionMonitor executionMonitor,
            X402PaymentMetrics paymentMetrics
    ) {
        this.orchestrator = orchestrator;
        this.discoveryService = discoveryService;
        this.executionMonitor = executionMonitor;
        this.paymentMetrics = paymentMetrics;
    }

    @PostMapping("/api/demo/run")
    @Operation(summary = "Execute one-click end-to-end pipeline from prompt to planning, quote scoring, x402 settlement, execution, and receipt")
    public ResponseEntity<ApiResponse<UnifiedWorkflowResponse>> runDemoPipeline(@RequestBody UnifiedWorkflowRequest request) {
        if (request == null || request.getPrompt() == null || request.getPrompt().isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Prompt must be provided in request body"));
        }

        UnifiedWorkflowResponse response = orchestrator.runUnifiedPipeline(request);
        return ResponseEntity.ok(ApiResponse.success("End-to-end workflow pipeline completed successfully", response));
    }

    @GetMapping("/api/system/status")
    @Operation(summary = "Retrieve real-time system health and component status overview")
    public ResponseEntity<ApiResponse<SystemHealthDto>> getSystemStatus() {
        SystemHealthDto status = new SystemHealthDto();
        return ResponseEntity.ok(ApiResponse.success("System status retrieved successfully", status));
    }

    @GetMapping("/api/system/metrics")
    @Operation(summary = "Retrieve consolidated system metrics across all execution, payment, discovery, and registry modules")
    public ResponseEntity<ApiResponse<SystemMetricsDto>> getSystemMetrics() {
        Map<String, Object> execMetrics = executionMonitor.getMetrics();
        Map<String, Object> payMetrics = paymentMetrics.getMetrics();
        long registeredAgentsCount = discoveryService.discoverAllAgents().size();

        SystemMetricsDto metrics = new SystemMetricsDto(execMetrics, payMetrics, registeredAgentsCount);
        return ResponseEntity.ok(ApiResponse.success("System metrics retrieved successfully", metrics));
    }
}
