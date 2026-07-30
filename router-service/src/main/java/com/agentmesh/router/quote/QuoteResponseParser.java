package com.agentmesh.router.quote;

import com.agentmesh.router.model.Agent;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QuoteResponseParser {

    private static final Logger log = LoggerFactory.getLogger(QuoteResponseParser.class);
    private final ObjectMapper objectMapper;

    public QuoteResponseParser() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public AgentQuoteResponse parse(String rawJson, Agent agent, String taskId, String workflowId) {
        try {
            AgentQuoteResponse response = objectMapper.readValue(rawJson, AgentQuoteResponse.class);
            enrichQuoteResponse(response, agent, taskId, workflowId);
            return response;
        } catch (Exception e) {
            log.warn("Failed to parse quote JSON response from agent {}: {}", agent.getId(), e.getMessage());
            AgentQuoteResponse errorResponse = new AgentQuoteResponse();
            enrichQuoteResponse(errorResponse, agent, taskId, workflowId);
            errorResponse.setValid(false);
            errorResponse.setStatus("MALFORMED_RESPONSE");
            errorResponse.setRejectionReason("Failed to parse response JSON: " + e.getMessage());
            return errorResponse;
        }
    }

    public AgentQuoteResponse parseFromMap(Map<String, Object> map, Agent agent, String taskId, String workflowId) {
        try {
            AgentQuoteResponse response = objectMapper.convertValue(map, AgentQuoteResponse.class);
            enrichQuoteResponse(response, agent, taskId, workflowId);
            return response;
        } catch (Exception e) {
            log.warn("Failed to convert map to quote response for agent {}: {}", agent.getId(), e.getMessage());
            AgentQuoteResponse errorResponse = new AgentQuoteResponse();
            enrichQuoteResponse(errorResponse, agent, taskId, workflowId);
            errorResponse.setValid(false);
            errorResponse.setStatus("MALFORMED_RESPONSE");
            errorResponse.setRejectionReason("Failed to convert response payload: " + e.getMessage());
            return errorResponse;
        }
    }

    public void enrichQuoteResponse(AgentQuoteResponse response, Agent agent, String taskId, String workflowId) {
        if (response == null) return;

        if (response.getAgentId() == null || response.getAgentId().isBlank()) {
            response.setAgentId(agent.getId());
        }
        if (response.getAgentName() == null || response.getAgentName().isBlank()) {
            response.setAgentName(agent.getName());
        }
        if (taskId != null) {
            response.setTaskId(taskId);
        }
        if (workflowId != null) {
            response.setWorkflowId(workflowId);
        }
        if (response.getQuotedPrice() == null || response.getQuotedPrice() <= 0.0) {
            response.setQuotedPrice(agent.getBasePrice() != null ? agent.getBasePrice() : 50.0);
        }
        if (response.getReputation() == null || response.getReputation() <= 0.0) {
            response.setReputation(agent.getRating() != null ? agent.getRating() : 4.8);
        }
        if (response.getHealthScore() == null || response.getHealthScore() <= 0.0) {
            response.setHealthScore(agent.getHealthScore() != null ? agent.getHealthScore() : 100.0);
        }
        if (response.getCurrentLoad() == null) {
            response.setCurrentLoad(agent.getCurrentLoad() != null ? agent.getCurrentLoad() : 0.0);
        }
        if (response.getSuccessRate() == null || response.getSuccessRate() <= 0.0) {
            response.setSuccessRate(agent.getSuccessRate() != null ? agent.getSuccessRate() : 98.0);
        }
    }
}
