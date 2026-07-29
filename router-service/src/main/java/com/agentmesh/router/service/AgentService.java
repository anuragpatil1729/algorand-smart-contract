package com.agentmesh.router.service;

import com.agentmesh.router.dto.AgentRequest;
import com.agentmesh.router.dto.AgentResponse;

import java.util.List;

public interface AgentService {
    AgentResponse registerAgent(AgentRequest request);
    AgentResponse getAgentById(String id);
    List<AgentResponse> getAllAgents();
    AgentResponse updateAgentHealth(String id, String status);
    void deleteAgent(String id);
}
