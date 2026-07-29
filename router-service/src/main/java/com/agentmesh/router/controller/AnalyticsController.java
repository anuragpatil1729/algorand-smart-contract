package com.agentmesh.router.controller;

import com.agentmesh.router.dto.AnalyticsDto;
import com.agentmesh.router.service.AgentMeshService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AgentMeshService agentMeshService;

    public AnalyticsController(AgentMeshService agentMeshService) {
        this.agentMeshService = agentMeshService;
    }

    @GetMapping
    public AnalyticsDto getAnalytics() {
        return agentMeshService.getAnalytics();
    }
}
