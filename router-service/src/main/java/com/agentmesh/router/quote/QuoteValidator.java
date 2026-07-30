package com.agentmesh.router.quote;

import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class QuoteValidator {

    private static final Logger log = LoggerFactory.getLogger(QuoteValidator.class);

    public boolean validate(AgentQuoteResponse response) {
        if (response == null) return false;

        if (response.getAgentId() == null || response.getAgentId().isBlank()) {
            reject(response, "INVALID_AGENT_ID", "Agent ID must not be empty");
            return false;
        }

        if (response.getQuotedPrice() == null || response.getQuotedPrice() < 0.0) {
            reject(response, "INVALID_PRICE", "Quoted price must be non-negative");
            return false;
        }

        if (response.getEstimatedDuration() == null || response.getEstimatedDuration() <= 0) {
            reject(response, "INVALID_DURATION", "Estimated duration must be greater than 0 seconds");
            return false;
        }

        if (response.getConfidence() == null || response.getConfidence() < 0.0 || response.getConfidence() > 100.0) {
            reject(response, "INVALID_CONFIDENCE", "Confidence must be between 0.0 and 100.0");
            return false;
        }

        if (response.getHealthScore() != null && response.getHealthScore() <= 0.0) {
            reject(response, "AGENT_UNHEALTHY", "Health score must be greater than 0");
            return false;
        }

        response.setValid(true);
        if (response.getStatus() == null || response.getStatus().isBlank()) {
            response.setStatus("VALID");
        }
        return true;
    }

    private void reject(AgentQuoteResponse response, String status, String reason) {
        response.setValid(false);
        response.setStatus(status);
        response.setRejectionReason(reason);
        log.warn("Quote validation rejected for agent '{}' (task '{}'): {} - {}", 
                response.getAgentId(), response.getTaskId(), status, reason);
    }
}
