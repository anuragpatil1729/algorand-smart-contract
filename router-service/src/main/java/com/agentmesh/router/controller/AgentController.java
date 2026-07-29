package com.agentmesh.router.controller;

import com.agentmesh.router.dto.AgentRequest;
import com.agentmesh.router.dto.AgentResponse;
import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.model.enums.HealthStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agents")
@Tag(name = "Agent Controller", description = "Endpoints for registering and managing AI microservice agents")
public class AgentController {

    @PostMapping
    @Operation(summary = "Register a new AI microservice agent")
    public ResponseEntity<ApiResponse<AgentResponse>> registerAgent(@Valid @RequestBody AgentRequest request) {
        String id = "agent-" + UUID.randomUUID().toString().substring(0, 8);
        AgentResponse placeholder = new AgentResponse(
                id,
                request.getName(),
                request.getDescription(),
                request.getEndpoint(),
                request.getWalletAddress(),
                4.5,
                95.0,
                request.getBasePrice(),
                HealthStatus.UP,
                request.getCapabilities() != null ? request.getCapabilities() : List.of("GENERAL"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Agent registered successfully", placeholder));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve registered agent details by ID")
    public ResponseEntity<ApiResponse<AgentResponse>> getAgentById(@PathVariable String id) {
        AgentResponse placeholder = new AgentResponse(
                id,
                "Sample Agent",
                "Placeholder Agent Description",
                "http://localhost:8001",
                "D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ",
                4.8,
                98.0,
                50.0,
                HealthStatus.UP,
                List.of("RESEARCH"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return ResponseEntity.ok(ApiResponse.success(placeholder));
    }

    @GetMapping
    @Operation(summary = "List all registered AI agents in the network")
    public ResponseEntity<ApiResponse<List<AgentResponse>>> getAllAgents() {
        return ResponseEntity.ok(ApiResponse.success("Agents retrieved successfully", new ArrayList<>()));
    }

    @PatchMapping("/{id}/health")
    @Operation(summary = "Update health status of an AI agent")
    public ResponseEntity<ApiResponse<AgentResponse>> updateAgentHealth(@PathVariable String id, @RequestParam String status) {
        HealthStatus hs = HealthStatus.UP;
        try {
            hs = HealthStatus.valueOf(status.toUpperCase());
        } catch (Exception ignored) {}

        AgentResponse placeholder = new AgentResponse(
                id,
                "Agent Name",
                "Description",
                "http://localhost:8001",
                "D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ",
                4.8,
                98.0,
                50.0,
                hs,
                List.of("GENERAL"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return ResponseEntity.ok(ApiResponse.success("Agent health status updated", placeholder));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deregister an AI agent from the network")
    public ResponseEntity<ApiResponse<Void>> deleteAgent(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Agent deregistered successfully", null));
    }
}
