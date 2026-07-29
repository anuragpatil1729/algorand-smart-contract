package com.agentmesh.router;

import com.agentmesh.router.dto.AgentRegistrationRequestDto;
import com.agentmesh.router.dto.AgentRegistryDto;
import com.agentmesh.router.dto.HeartbeatRequestDto;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.model.enums.HealthStatus;
import com.agentmesh.router.repository.AgentEventRepository;
import com.agentmesh.router.repository.AgentRepository;
import com.agentmesh.router.service.HealthMonitorService;
import com.agentmesh.router.service.RegistryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistryServiceTest {

    private AgentRepository agentRepository;
    private AgentEventRepository eventRepository;
    private HealthMonitorService healthMonitorService;
    private RegistryService registryService;

    @BeforeEach
    void setUp() {
        agentRepository = mock(AgentRepository.class);
        eventRepository = mock(AgentEventRepository.class);
        healthMonitorService = new HealthMonitorService();
        registryService = new RegistryService(agentRepository, eventRepository, healthMonitorService);
    }

    @Test
    void testRegisterNewAgent() {
        AgentRegistrationRequestDto req = new AgentRegistrationRequestDto();
        req.setId("test-agent-1");
        req.setName("Test Agent");
        req.setEndpoint("http://localhost:9001");
        req.setCapabilities(List.of("TEST", "QA"));
        req.setBasePrice(40.0);

        when(agentRepository.findById("test-agent-1")).thenReturn(Optional.empty());
        when(agentRepository.save(any(Agent.class))).thenAnswer(inv -> inv.getArgument(0));

        AgentRegistryDto result = registryService.registerAgent(req);

        assertNotNull(result);
        assertEquals("test-agent-1", result.getId());
        assertEquals("Test Agent", result.getName());
        assertEquals("ONLINE", result.getStatus());
        assertEquals(2, result.getCapabilities().size());
        verify(agentRepository).save(any(Agent.class));
    }

    @Test
    void testProcessHeartbeat() {
        Agent existingAgent = Agent.builder()
                .id("test-agent-1")
                .name("Test Agent")
                .endpoint("http://localhost:9001")
                .healthStatus(HealthStatus.ONLINE)
                .healthScore(100.0)
                .lastHeartbeat(LocalDateTime.now().minusSeconds(10))
                .build();

        when(agentRepository.findById("test-agent-1")).thenReturn(Optional.of(existingAgent));
        when(agentRepository.save(any(Agent.class))).thenAnswer(inv -> inv.getArgument(0));

        HeartbeatRequestDto heartbeat = new HeartbeatRequestDto();
        heartbeat.setAgentId("test-agent-1");
        heartbeat.setCpuUsagePercent(12.5);
        heartbeat.setMemoryUsageMb(150.0);
        heartbeat.setRunningTasks(1);
        heartbeat.setQueueSize(0);

        AgentRegistryDto result = registryService.processHeartbeat(heartbeat);

        assertNotNull(result);
        assertEquals(12.5, result.getCpuUsage());
        assertEquals(1, result.getRunningTasks());
        assertNotNull(result.getLastHeartbeat());
    }

    @Test
    void testDeregisterAgent() {
        Agent existingAgent = Agent.builder()
                .id("test-agent-1")
                .name("Test Agent")
                .healthStatus(HealthStatus.ONLINE)
                .build();

        when(agentRepository.findById("test-agent-1")).thenReturn(Optional.of(existingAgent));

        registryService.deregisterAgent("test-agent-1");

        ArgumentCaptor<Agent> captor = ArgumentCaptor.forClass(Agent.class);
        verify(agentRepository).save(captor.capture());
        assertEquals(HealthStatus.OFFLINE, captor.getValue().getHealthStatusEnum());
    }
}
