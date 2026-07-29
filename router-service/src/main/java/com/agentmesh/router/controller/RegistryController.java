package com.agentmesh.router.controller;

import com.agentmesh.router.dto.*;
import com.agentmesh.router.service.RegistryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registry")
@Tag(name = "Agent Registry Controller", description = "Dynamic Registration, Heartbeat, and Service Discovery Endpoints")
public class RegistryController {

    private final RegistryService registryService;

    public RegistryController(RegistryService registryService) {
        this.registryService = registryService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register an AI agent in the network")
    public ResponseEntity<ApiResponse<AgentRegistryDto>> registerAgent(@RequestBody AgentRegistrationRequestDto request) {
        AgentRegistryDto result = registryService.registerAgent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Agent registered successfully", result));
    }

    @PostMapping("/deregister")
    @Operation(summary = "Deregister an AI agent")
    public ResponseEntity<ApiResponse<String>> deregisterAgent(@RequestBody Map<String, String> payload) {
        String agentId = payload.get("agentId");
        if (agentId == null) {
            agentId = payload.get("id");
        }
        if (agentId != null) {
            registryService.deregisterAgent(agentId);
            return ResponseEntity.ok(ApiResponse.success("Agent deregistered successfully", agentId));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Agent ID required"));
    }

    @PostMapping("/heartbeat")
    @Operation(summary = "Receive agent telemetry heartbeat")
    public ResponseEntity<ApiResponse<AgentRegistryDto>> processHeartbeat(@RequestBody HeartbeatRequestDto heartbeat) {
        AgentRegistryDto result = registryService.processHeartbeat(heartbeat);
        if (result != null) {
            return ResponseEntity.ok(ApiResponse.success("Heartbeat processed", result));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Agent not found in registry"));
    }

    @GetMapping("/agents")
    @Operation(summary = "List all registered agents")
    public ResponseEntity<ApiResponse<List<AgentRegistryDto>>> getAllAgents() {
        List<AgentRegistryDto> agents = registryService.getAllAgents();
        return ResponseEntity.ok(ApiResponse.success("Agents retrieved successfully", agents));
    }

    @GetMapping("/online")
    @Operation(summary = "List online/available agents")
    public ResponseEntity<ApiResponse<List<AgentRegistryDto>>> getOnlineAgents() {
        List<AgentRegistryDto> online = registryService.getOnlineAgents();
        return ResponseEntity.ok(ApiResponse.success("Online agents retrieved", online));
    }

    @GetMapping("/capability/{capability}")
    @Operation(summary = "Find agents by capability")
    public ResponseEntity<ApiResponse<List<AgentRegistryDto>>> getAgentsByCapability(@PathVariable String capability) {
        List<AgentRegistryDto> matching = registryService.getAgentsByCapability(capability);
        return ResponseEntity.ok(ApiResponse.success("Capability search results", matching));
    }

    @GetMapping("/{agentId}")
    @Operation(summary = "Get agent details by ID")
    public ResponseEntity<ApiResponse<AgentRegistryDto>> getAgentById(@PathVariable String agentId) {
        AgentRegistryDto agent = registryService.getAgentById(agentId);
        if (agent != null) {
            return ResponseEntity.ok(ApiResponse.success("Agent found", agent));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Agent not found with ID: " + agentId));
    }

    @PutMapping("/{agentId}")
    @Operation(summary = "Update agent registration metadata")
    public ResponseEntity<ApiResponse<AgentRegistryDto>> updateAgent(
            @PathVariable String agentId,
            @RequestBody AgentRegistrationRequestDto request
    ) {
        request.setId(agentId);
        AgentRegistryDto updated = registryService.registerAgent(request);
        return ResponseEntity.ok(ApiResponse.success("Agent updated successfully", updated));
    }

    @DeleteMapping("/{agentId}")
    @Operation(summary = "Delete an agent from registry")
    public ResponseEntity<ApiResponse<String>> deleteAgent(@PathVariable String agentId) {
        registryService.deleteAgent(agentId);
        return ResponseEntity.ok(ApiResponse.success("Agent deleted from registry", agentId));
    }

    @GetMapping("/dashboard/stats")
    @Operation(summary = "Get telemetry dashboard statistics")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getDashboardStats() {
        DashboardStatsDto stats = registryService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats retrieved", stats));
    }
}
