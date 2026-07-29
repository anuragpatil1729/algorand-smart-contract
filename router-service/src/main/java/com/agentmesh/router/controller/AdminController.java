package com.agentmesh.router.controller;

import com.agentmesh.router.model.ExecutionLog;
import com.agentmesh.router.model.ScoringConfig;
import com.agentmesh.router.service.AgentMeshService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AgentMeshService agentMeshService;

    public AdminController(AgentMeshService agentMeshService) {
        this.agentMeshService = agentMeshService;
    }

    @GetMapping("/config/scoring")
    public ScoringConfig getScoringConfig() {
        return agentMeshService.getScoringConfig();
    }

    @PutMapping("/config/scoring")
    public ScoringConfig updateScoringConfig(@RequestBody ScoringConfig config) {
        return agentMeshService.updateScoringConfig(config);
    }

    @GetMapping("/logs")
    public List<ExecutionLog> getExecutionLogs(@RequestParam(required = false) String workflowId) {
        return agentMeshService.getLogs(workflowId);
    }
}
