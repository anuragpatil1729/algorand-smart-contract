package com.agentmesh.router.controller;

import com.agentmesh.router.dto.AgentDto;
import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.service.AgentMeshService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@Tag(name = "Agent Controller", description = "Endpoints for registering and managing AI microservice agents")
public class AgentController {

    private final AgentMeshService agentMeshService;

    public AgentController(AgentMeshService agentMeshService) {
        this.agentMeshService = agentMeshService;
    }

    @PostMapping
    @Operation(summary = "Register a new AI microservice agent")
    public ResponseEntity<ApiResponse<AgentDto>> registerAgent(@RequestBody AgentDto request) {
        AgentDto result = agentMeshService.registerAgent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Agent registered successfully", result));
    }

    @GetMapping
    @Operation(summary = "List all registered AI agents in the network")
    public ResponseEntity<ApiResponse<List<AgentDto>>> getAllAgents() {
        List<AgentDto> agents = agentMeshService.getAllAgents();
        return ResponseEntity.ok(ApiResponse.success("Agents retrieved successfully", agents));
    }
}
