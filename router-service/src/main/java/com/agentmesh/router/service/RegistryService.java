package com.agentmesh.router.service;

import com.agentmesh.router.dto.*;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.model.AgentEvent;
import com.agentmesh.router.model.enums.HealthStatus;
import com.agentmesh.router.repository.AgentEventRepository;
import com.agentmesh.router.repository.AgentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegistryService {

    private static final Logger log = LoggerFactory.getLogger(RegistryService.class);

    private final AgentRepository agentRepository;
    private final AgentEventRepository eventRepository;
    private final HealthMonitorService healthMonitorService;

    public RegistryService(AgentRepository agentRepository, AgentEventRepository eventRepository, HealthMonitorService healthMonitorService) {
        this.agentRepository = agentRepository;
        this.eventRepository = eventRepository;
        this.healthMonitorService = healthMonitorService;
    }

    @Transactional
    public AgentRegistryDto registerAgent(AgentRegistrationRequestDto request) {
        String agentId = request.getId() != null ? request.getId() : "agent-" + UUID.randomUUID().toString().substring(0, 8);

        Optional<Agent> existingOpt = agentRepository.findById(agentId);
        Agent agent;

        String capabilitiesStr = request.getCapabilities() != null ? String.join(",", request.getCapabilities()) : "";

        if (existingOpt.isPresent()) {
            agent = existingOpt.get();
            agent.setName(request.getName());
            if (request.getDescription() != null) agent.setDescription(request.getDescription());
            if (request.getEndpoint() != null) agent.setEndpoint(request.getEndpoint());
            if (request.getWalletAddress() != null) agent.setWalletAddress(request.getWalletAddress());
            if (request.getVersion() != null) agent.setVersion(request.getVersion());
            if (request.getMaxConcurrency() != null) agent.setMaxConcurrency(request.getMaxConcurrency());
            if (request.getBasePrice() != null) agent.setBasePrice(request.getBasePrice());
            if (!capabilitiesStr.isBlank()) agent.setCapabilities(capabilitiesStr);
            agent.setHealthStatus(HealthStatus.ONLINE);
            agent.setLastHeartbeat(LocalDateTime.now());
            log.info("Updated existing agent registration: {}", agentId);
        } else {
            agent = Agent.builder()
                    .id(agentId)
                    .name(request.getName())
                    .description(request.getDescription())
                    .endpoint(request.getEndpoint())
                    .walletAddress(request.getWalletAddress() != null ? request.getWalletAddress() : "")
                    .version(request.getVersion() != null ? request.getVersion() : "1.0.0")
                    .capabilities(capabilitiesStr)
                    .maxConcurrency(request.getMaxConcurrency() != null ? request.getMaxConcurrency() : 5)
                    .basePrice(request.getBasePrice() != null ? request.getBasePrice() : 50.0)
                    .healthStatus(HealthStatus.ONLINE)
                    .healthScore(100.0)
                    .registrationTime(LocalDateTime.now())
                    .lastHeartbeat(LocalDateTime.now())
                    .build();
            log.info("Registered new agent: {}", agentId);
        }

        Agent saved = agentRepository.save(agent);
        logEvent(saved.getId(), "AGENT_REGISTERED", "Agent '" + saved.getName() + "' successfully registered", "INFO");

        return AgentRegistryDto.fromEntity(saved);
    }

    @Transactional
    public void deregisterAgent(String agentId) {
        Optional<Agent> opt = agentRepository.findById(agentId);
        if (opt.isPresent()) {
            Agent agent = opt.get();
            agent.setHealthStatus(HealthStatus.OFFLINE);
            agent.setHealthScore(0.0);
            agentRepository.save(agent);
            logEvent(agentId, "AGENT_DEREGISTERED", "Agent '" + agent.getName() + "' deregistered", "INFO");
            log.info("Agent {} deregistered successfully", agentId);
        }
    }

    @Transactional
    public AgentRegistryDto processHeartbeat(HeartbeatRequestDto heartbeat) {
        String agentId = heartbeat.getAgentId();
        if (agentId == null) return null;

        Optional<Agent> opt = agentRepository.findById(agentId);
        if (opt.isEmpty()) {
            log.warn("Heartbeat received for unregistered agent: {}", agentId);
            return null;
        }

        Agent agent = opt.get();
        HealthStatus previousStatus = agent.getHealthStatusEnum();

        agent.setLastHeartbeat(LocalDateTime.now());
        if (heartbeat.getCpuUsagePercent() != null) agent.setCpuUsage(heartbeat.getCpuUsagePercent());
        if (heartbeat.getMemoryUsageMb() != null) agent.setMemoryUsage(heartbeat.getMemoryUsageMb());
        if (heartbeat.getRunningTasks() != null) agent.setRunningTasks(heartbeat.getRunningTasks());
        if (heartbeat.getQueueSize() != null) agent.setQueueSize(heartbeat.getQueueSize());

        // Calculate load % based on running tasks / max concurrency
        int maxConc = agent.getMaxConcurrency() != null && agent.getMaxConcurrency() > 0 ? agent.getMaxConcurrency() : 5;
        int running = agent.getRunningTasks() != null ? agent.getRunningTasks() : 0;
        double calculatedLoad = Math.min(100.0, Math.round(((double) running / maxConc) * 100.0));
        agent.setCurrentLoad(calculatedLoad);

        // Update health score and status
        double healthScore = healthMonitorService.calculateHealthScore(agent);
        agent.setHealthScore(healthScore);

        HealthStatus newStatus = healthMonitorService.evaluateStatus(agent);
        agent.setHealthStatus(newStatus);

        Agent saved = agentRepository.save(agent);

        if (previousStatus != newStatus) {
            logEvent(agentId, "HEALTH_CHANGED", "Health status changed from " + previousStatus + " to " + newStatus, "WARN");
        }

        return AgentRegistryDto.fromEntity(saved);
    }

    public List<AgentRegistryDto> getAllAgents() {
        return agentRepository.findAll().stream()
                .map(AgentRegistryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AgentRegistryDto> getOnlineAgents() {
        return agentRepository.findAll().stream()
                .filter(a -> a.getHealthStatusEnum() == HealthStatus.ONLINE || a.getHealthStatusEnum() == HealthStatus.UP || a.getHealthStatusEnum() == HealthStatus.BUSY)
                .map(AgentRegistryDto::fromEntity)
                .collect(Collectors.toList());
    }

    public AgentRegistryDto getAgentById(String agentId) {
        return agentRepository.findById(agentId)
                .map(AgentRegistryDto::fromEntity)
                .orElse(null);
    }

    public List<AgentRegistryDto> getAgentsByCapability(String capability) {
        if (capability == null || capability.isBlank()) return Collections.emptyList();
        String capUpper = capability.toUpperCase();
        return agentRepository.findAll().stream()
                .filter(a -> a.getCapabilities() != null && a.getCapabilities().toUpperCase().contains(capUpper))
                .map(AgentRegistryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAgent(String agentId) {
        agentRepository.deleteById(agentId);
        logEvent(agentId, "AGENT_DELETED", "Agent record deleted from registry", "WARN");
    }

    public DashboardStatsDto getDashboardStats() {
        List<Agent> agents = agentRepository.findAll();
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalAgents(agents.size());

        long online = agents.stream().filter(a -> a.getHealthStatusEnum() == HealthStatus.ONLINE || a.getHealthStatusEnum() == HealthStatus.UP).count();
        long busy = agents.stream().filter(a -> a.getHealthStatusEnum() == HealthStatus.BUSY).count();
        long degraded = agents.stream().filter(a -> a.getHealthStatusEnum() == HealthStatus.DEGRADED).count();
        long offline = agents.stream().filter(a -> a.getHealthStatusEnum() == HealthStatus.OFFLINE || a.getHealthStatusEnum() == HealthStatus.DOWN).count();

        stats.setOnlineAgents(online);
        stats.setBusyAgents(busy);
        stats.setDegradedAgents(degraded);
        stats.setOfflineAgents(offline);

        double avgHealth = agents.stream().mapToDouble(a -> a.getHealthScore() != null ? a.getHealthScore() : 100.0).average().orElse(100.0);
        double avgResp = agents.stream().mapToDouble(a -> a.getAverageResponseTime() != null ? a.getAverageResponseTime() : 50.0).average().orElse(50.0);
        double avgPrice = agents.stream().mapToDouble(a -> a.getBasePrice() != null ? a.getBasePrice() : 50.0).average().orElse(50.0);

        stats.setAverageHealthScore(Math.round(avgHealth * 10.0) / 10.0);
        stats.setAverageResponseTime(Math.round(avgResp * 10.0) / 10.0);
        stats.setAverageBasePrice(Math.round(avgPrice * 100.0) / 100.0);

        Map<String, Integer> capCounts = new HashMap<>();
        Map<String, Double> loads = new HashMap<>();
        for (Agent a : agents) {
            if (a.getCapabilities() != null) {
                for (String c : a.getCapabilities().split(",")) {
                    String trimmed = c.trim();
                    if (!trimmed.isEmpty()) {
                        capCounts.put(trimmed, capCounts.getOrDefault(trimmed, 0) + 1);
                    }
                }
            }
            loads.put(a.getName(), a.getCurrentLoad() != null ? a.getCurrentLoad() : 0.0);
        }
        stats.setCapabilityCounts(capCounts);
        stats.setAgentLoads(loads);

        return stats;
    }

    private void logEvent(String agentId, String type, String message, String level) {
        try {
            AgentEvent event = new AgentEvent(UUID.randomUUID().toString(), agentId, type, message, level, LocalDateTime.now());
            eventRepository.save(event);
        } catch (Exception e) {
            log.error("Failed to log agent event for {}", agentId, e);
        }
    }
}
