package com.agentmesh.router.controller;

import com.agentmesh.router.dto.AnalyticsSummaryDto;
import com.agentmesh.router.service.AgentMeshService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AgentMeshService agentMeshService;

    public AnalyticsController(AgentMeshService agentMeshService) {
        this.agentMeshService = agentMeshService;
    }

    @GetMapping
    public ResponseEntity<AnalyticsSummaryDto> getAnalytics() {
        AnalyticsSummaryDto summary = agentMeshService.getAnalyticsSummary();
        return ResponseEntity.ok(summary);
    }
}
