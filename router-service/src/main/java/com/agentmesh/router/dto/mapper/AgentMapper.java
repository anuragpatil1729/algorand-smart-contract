package com.agentmesh.router.dto.mapper;

import com.agentmesh.router.dto.AgentRequest;
import com.agentmesh.router.dto.AgentResponse;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.model.enums.HealthStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AgentMapper {

    public static Agent toEntity(AgentRequest request) {
        if (request == null) return null;
        String capsStr = request.getCapabilities() != null ? String.join(",", request.getCapabilities()) : "GENERAL";
        return Agent.builder()
                .id("agent-" + UUID.randomUUID().toString().substring(0, 8))
                .name(request.getName())
                .description(request.getDescription())
                .endpoint(request.getEndpoint())
                .walletAddress(request.getWalletAddress())
                .basePrice(request.getBasePrice())
                .healthStatus(HealthStatus.UP)
                .capabilities(capsStr)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static AgentResponse toDto(Agent agent) {
        if (agent == null) return null;
        List<String> caps = agent.getCapabilities() != null ? 
                Arrays.stream(agent.getCapabilities().split(",")).map(String::trim).collect(Collectors.toList()) : 
                List.of("GENERAL");

        return new AgentResponse(
                agent.getId(),
                agent.getName(),
                agent.getDescription(),
                agent.getEndpoint(),
                agent.getWalletAddress(),
                agent.getRating(),
                agent.getSuccessRate(),
                agent.getBasePrice(),
                agent.getHealthStatusEnum(),
                caps,
                agent.getCreatedAt(),
                agent.getUpdatedAt()
        );
    }
}
