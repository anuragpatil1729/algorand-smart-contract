package com.agentmesh.router.quote;

import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ScoringEngineTest {

    private ScoringEngine scoringEngine;

    @BeforeEach
    void setUp() {
        scoringEngine = new ScoringEngine();
    }

    @Test
    void testDefaultWeightsScoring() {
        AgentQuoteResponse q1 = new AgentQuoteResponse();
        q1.setAgentId("a1");
        q1.setQuotedPrice(100.0);
        q1.setEstimatedDuration(20);
        q1.setReputation(5.0);
        q1.setHealthScore(100.0);
        q1.setConfidence(95.0);
        q1.setCurrentLoad(0.0);
        q1.setValid(true);

        AgentQuoteResponse q2 = new AgentQuoteResponse();
        q2.setAgentId("a2");
        q2.setQuotedPrice(50.0);
        q2.setEstimatedDuration(10);
        q2.setReputation(4.0);
        q2.setHealthScore(90.0);
        q2.setConfidence(90.0);
        q2.setCurrentLoad(10.0);
        q2.setValid(true);

        List<AgentQuoteResponse> quotes = List.of(q1, q2);

        double score1 = scoringEngine.calculateScore(q1, quotes);
        double score2 = scoringEngine.calculateScore(q2, quotes);

        assertTrue(score1 > 0.0);
        assertTrue(score2 > 0.0);
    }

    @Test
    void testCustomWeightsScoring() {
        AgentQuoteResponse q1 = new AgentQuoteResponse();
        q1.setAgentId("a1");
        q1.setQuotedPrice(100.0);
        q1.setValid(true);

        AgentQuoteResponse q2 = new AgentQuoteResponse();
        q2.setAgentId("a2");
        q2.setQuotedPrice(20.0);
        q2.setValid(true);

        List<AgentQuoteResponse> quotes = List.of(q1, q2);

        // Heavy price weight (80% price)
        ScoringEngine.Weights priceHeavy = scoringEngine.parseWeightsMap(Map.of("price", 0.8, "reputation", 0.2));

        double score1 = scoringEngine.calculateScore(q1, quotes, priceHeavy);
        double score2 = scoringEngine.calculateScore(q2, quotes, priceHeavy);

        // Cheaper quote (q2) should receive higher score under price heavy weights
        assertTrue(score2 > score1);
    }

    @Test
    void testInvalidQuoteReceivesZeroScore() {
        AgentQuoteResponse invalidQuote = new AgentQuoteResponse();
        invalidQuote.setValid(false);

        double score = scoringEngine.calculateScore(invalidQuote, List.of(invalidQuote));

        assertEquals(0.0, score);
    }
}
