package com.agentmesh.router.controller;

import com.agentmesh.router.dto.ScoringConfigDto;
import com.agentmesh.router.model.ExecutionLog;
import com.agentmesh.router.service.AgentMeshService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AgentMeshService agentMeshService;

    public AdminController(AgentMeshService agentMeshService) {
        this.agentMeshService = agentMeshService;
    }

    @GetMapping("/logs")
    public ResponseEntity<List<ExecutionLog>> getLogs(@RequestParam(required = false) String workflowId) {
        List<ExecutionLog> logs = agentMeshService.getLogs(workflowId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/scoring-config")
    public ResponseEntity<ScoringConfigDto> getScoringConfig() {
        ScoringConfigDto config = agentMeshService.getScoringConfig();
        return ResponseEntity.ok(config);
    }

    @PostMapping("/scoring-config")
    public ResponseEntity<ScoringConfigDto> updateScoringConfig(@RequestBody ScoringConfigDto configDto) {
        ScoringConfigDto updated = agentMeshService.updateScoringConfig(configDto);
        return ResponseEntity.ok(updated);
    }
}
