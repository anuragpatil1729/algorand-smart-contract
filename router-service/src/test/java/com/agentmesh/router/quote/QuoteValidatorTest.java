package com.agentmesh.router.quote;

import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuoteValidatorTest {

    private QuoteValidator validator;

    @BeforeEach
    void setUp() {
        validator = new QuoteValidator();
    }

    @Test
    void testValidQuoteResponse() {
        AgentQuoteResponse response = new AgentQuoteResponse();
        response.setAgentId("agent-01");
        response.setQuotedPrice(45.0);
        response.setEstimatedDuration(10);
        response.setConfidence(95.0);
        response.setHealthScore(100.0);

        boolean valid = validator.validate(response);

        assertTrue(valid);
        assertTrue(response.getValid());
    }

    @Test
    void testInvalidQuoteResponseMissingAgentId() {
        AgentQuoteResponse response = new AgentQuoteResponse();
        response.setAgentId("");
        response.setQuotedPrice(45.0);
        response.setEstimatedDuration(10);

        boolean valid = validator.validate(response);

        assertFalse(valid);
        assertFalse(response.getValid());
        assertEquals("INVALID_AGENT_ID", response.getStatus());
    }

    @Test
    void testInvalidQuoteResponseNegativePrice() {
        AgentQuoteResponse response = new AgentQuoteResponse();
        response.setAgentId("agent-01");
        response.setQuotedPrice(-10.0);

        boolean valid = validator.validate(response);

        assertFalse(valid);
        assertEquals("INVALID_PRICE", response.getStatus());
    }

    @Test
    void testInvalidQuoteResponseZeroDuration() {
        AgentQuoteResponse response = new AgentQuoteResponse();
        response.setAgentId("agent-01");
        response.setQuotedPrice(10.0);
        response.setEstimatedDuration(0);

        boolean valid = validator.validate(response);

        assertFalse(valid);
        assertEquals("INVALID_DURATION", response.getStatus());
    }

    @Test
    void testInvalidQuoteResponseConfidenceOutOfRange() {
        AgentQuoteResponse response = new AgentQuoteResponse();
        response.setAgentId("agent-01");
        response.setQuotedPrice(10.0);
        response.setEstimatedDuration(5);
        response.setConfidence(150.0);

        boolean valid = validator.validate(response);

        assertFalse(valid);
        assertEquals("INVALID_CONFIDENCE", response.getStatus());
    }
}
