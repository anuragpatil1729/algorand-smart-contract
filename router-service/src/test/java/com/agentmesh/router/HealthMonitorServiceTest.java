package com.agentmesh.router;

import com.agentmesh.router.model.Agent;
import com.agentmesh.router.model.enums.HealthStatus;
import com.agentmesh.router.service.HealthMonitorService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HealthMonitorServiceTest {

    private final HealthMonitorService healthMonitorService = new HealthMonitorService();

    @Test
    void testHealthyAgentEvaluation() {
        Agent agent = Agent.builder()
                .id("a1")
                .name("Healthy Agent")
                .cpuUsage(15.0)
                .memoryUsage(120.0)
                .runningTasks(1)
                .maxConcurrency(5)
                .queueSize(0)
                .lastHeartbeat(LocalDateTime.now())
                .build();

        double score = healthMonitorService.calculateHealthScore(agent);
        HealthStatus status = healthMonitorService.evaluateStatus(agent);

        assertEquals(100.0, score);
        assertEquals(HealthStatus.ONLINE, status);
    }

    @Test
    void testHeartbeatTimeoutStatusEvaluation() {
        Agent staleAgent = Agent.builder()
                .id("a2")
                .name("Stale Agent")
                .lastHeartbeat(LocalDateTime.now().minusSeconds(120))
                .build();

        double score = healthMonitorService.calculateHealthScore(staleAgent);
        HealthStatus status = healthMonitorService.evaluateStatus(staleAgent);

        assertEquals(0.0, score);
        assertEquals(HealthStatus.OFFLINE, status);
    }

    @Test
    void testHighLoadStatusEvaluation() {
        Agent busyAgent = Agent.builder()
                .id("a3")
                .name("Busy Agent")
                .runningTasks(5)
                .maxConcurrency(5)
                .currentLoad(100.0)
                .lastHeartbeat(LocalDateTime.now())
                .build();

        HealthStatus status = healthMonitorService.evaluateStatus(busyAgent);
        assertEquals(HealthStatus.BUSY, status);
    }
}
