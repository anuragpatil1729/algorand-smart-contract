package com.agentmesh.router.scheduler;

import com.agentmesh.router.model.Agent;
import com.agentmesh.router.model.AgentEvent;
import com.agentmesh.router.model.enums.HealthStatus;
import com.agentmesh.router.repository.AgentEventRepository;
import com.agentmesh.router.repository.AgentRepository;
import com.agentmesh.router.service.HealthMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class HeartbeatScheduler {

    private static final Logger log = LoggerFactory.getLogger(HeartbeatScheduler.class);

    private final AgentRepository agentRepository;
    private final AgentEventRepository eventRepository;
    private final HealthMonitorService healthMonitorService;

    public HeartbeatScheduler(AgentRepository agentRepository, AgentEventRepository eventRepository, HealthMonitorService healthMonitorService) {
        this.agentRepository = agentRepository;
        this.eventRepository = eventRepository;
        this.healthMonitorService = healthMonitorService;
    }

    @Scheduled(fixedRate = 15000)
    @Transactional
    public void monitorAgentHeartbeatsAndHealth() {
        List<Agent> agents = agentRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Agent agent : agents) {
            HealthStatus currentStatus = agent.getHealthStatusEnum();
            LocalDateTime lastBeat = agent.getLastHeartbeat();

            if (lastBeat != null) {
                long secondsSince = Duration.between(lastBeat, now).getSeconds();
                if (secondsSince > 90 && currentStatus != HealthStatus.OFFLINE && currentStatus != HealthStatus.DOWN) {
                    agent.setHealthStatus(HealthStatus.OFFLINE);
                    agent.setHealthScore(0.0);
                    agentRepository.save(agent);
                    logEvent(agent.getId(), "AGENT_OFFLINE", "Agent '" + agent.getName() + "' heartbeat timeout (" + secondsSince + "s ago)", "WARN");
                    log.warn("Agent {} marked OFFLINE due to heartbeat timeout ({}s ago)", agent.getId(), secondsSince);
                    continue;
                }
            }

            double newHealthScore = healthMonitorService.calculateHealthScore(agent);
            HealthStatus newStatus = healthMonitorService.evaluateStatus(agent);

            if (currentStatus != newStatus || Math.abs(agent.getHealthScore() - newHealthScore) > 5.0) {
                agent.setHealthScore(newHealthScore);
                agent.setHealthStatus(newStatus);
                agentRepository.save(agent);

                if (currentStatus != newStatus) {
                    logEvent(agent.getId(), "HEALTH_CHANGED", "Agent status changed from " + currentStatus + " to " + newStatus, "INFO");
                    log.info("Agent {} status changed: {} -> {}", agent.getId(), currentStatus, newStatus);
                }
            }
        }
    }

    private void logEvent(String agentId, String type, String message, String level) {
        try {
            AgentEvent event = new AgentEvent(UUID.randomUUID().toString(), agentId, type, message, level, LocalDateTime.now());
            eventRepository.save(event);
        } catch (Exception e) {
            log.error("Failed to log event for agent {}", agentId, e);
        }
    }
}
