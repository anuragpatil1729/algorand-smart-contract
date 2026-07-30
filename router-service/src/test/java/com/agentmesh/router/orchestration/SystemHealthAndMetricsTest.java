package com.agentmesh.router.orchestration;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.execution.ExecutionEventBus;
import com.agentmesh.router.execution.ExecutionMonitor;
import com.agentmesh.router.orchestration.dto.SystemHealthDto;
import com.agentmesh.router.orchestration.dto.SystemMetricsDto;
import com.agentmesh.router.x402.service.X402PaymentMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SystemHealthAndMetricsTest {

    private AgentDiscoveryService discoveryService;
    private ExecutionMonitor executionMonitor;
    private X402PaymentMetrics paymentMetrics;

    @BeforeEach
    void setUp() {
        discoveryService = mock(AgentDiscoveryService.class);
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        ExecutionEventBus eventBus = new ExecutionEventBus(messagingTemplate);
        executionMonitor = new ExecutionMonitor(eventBus);
        paymentMetrics = new X402PaymentMetrics();
    }

    @Test
    void testSystemHealthStatusDto() {
        SystemHealthDto status = new SystemHealthDto();

        assertEquals("HEALTHY", status.getOverallStatus());
        assertNotNull(status.getComponents());
        assertEquals("UP", status.getComponents().get("planner"));
        assertEquals("UP", status.getComponents().get("executionEngine"));
        assertEquals("UP", status.getComponents().get("x402Middleware"));
        assertEquals("UP", status.getComponents().get("algorandProvider"));
    }

    @Test
    void testSystemMetricsDto() {
        when(discoveryService.discoverAllAgents()).thenReturn(Collections.emptyList());

        SystemMetricsDto metrics = new SystemMetricsDto(
                executionMonitor.getMetrics(),
                paymentMetrics.getMetrics(),
                (long) discoveryService.discoverAllAgents().size()
        );

        assertNotNull(metrics.getExecutionMetrics());
        assertNotNull(metrics.getPaymentMetrics());
        assertEquals(0L, metrics.getRegisteredAgentsCount());
    }
}
