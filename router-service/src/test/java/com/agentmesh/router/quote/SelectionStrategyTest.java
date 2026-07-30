package com.agentmesh.router.quote;

import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import com.agentmesh.router.quote.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SelectionStrategyTest {

    private ScoringEngine scoringEngine;
    private BalancedStrategy balancedStrategy;
    private LowestPriceStrategy lowestPriceStrategy;
    private HighestQualityStrategy highestQualityStrategy;
    private FastestCompletionStrategy fastestCompletionStrategy;
    private LeastLoadedStrategy leastLoadedStrategy;
    private CustomSelectionStrategy customStrategy;

    private AgentQuoteResponse cheapSlowQuote;
    private AgentQuoteResponse expensiveFastQuote;
    private AgentQuoteResponse highQualityQuote;

    @BeforeEach
    void setUp() {
        scoringEngine = new ScoringEngine();
        balancedStrategy = new BalancedStrategy();
        lowestPriceStrategy = new LowestPriceStrategy();
        highestQualityStrategy = new HighestQualityStrategy();
        fastestCompletionStrategy = new FastestCompletionStrategy();
        leastLoadedStrategy = new LeastLoadedStrategy();
        customStrategy = new CustomSelectionStrategy();

        cheapSlowQuote = new AgentQuoteResponse();
        cheapSlowQuote.setAgentId("cheap-slow");
        cheapSlowQuote.setQuotedPrice(10.0);
        cheapSlowQuote.setEstimatedDuration(100);
        cheapSlowQuote.setReputation(4.0);
        cheapSlowQuote.setHealthScore(90.0);
        cheapSlowQuote.setConfidence(90.0);
        cheapSlowQuote.setCurrentLoad(50.0);
        cheapSlowQuote.setValid(true);

        expensiveFastQuote = new AgentQuoteResponse();
        expensiveFastQuote.setAgentId("expensive-fast");
        expensiveFastQuote.setQuotedPrice(100.0);
        expensiveFastQuote.setEstimatedDuration(5);
        expensiveFastQuote.setReputation(4.5);
        expensiveFastQuote.setHealthScore(95.0);
        expensiveFastQuote.setConfidence(95.0);
        expensiveFastQuote.setCurrentLoad(20.0);
        expensiveFastQuote.setValid(true);

        highQualityQuote = new AgentQuoteResponse();
        highQualityQuote.setAgentId("high-quality");
        highQualityQuote.setQuotedPrice(50.0);
        highQualityQuote.setEstimatedDuration(20);
        highQualityQuote.setReputation(5.0);
        highQualityQuote.setHealthScore(100.0);
        highQualityQuote.setConfidence(99.0);
        highQualityQuote.setCurrentLoad(0.0);
        highQualityQuote.setValid(true);
    }

    @Test
    void testLowestPriceStrategy() {
        List<AgentQuoteResponse> quotes = List.of(expensiveFastQuote, cheapSlowQuote, highQualityQuote);
        AgentQuoteResponse selected = lowestPriceStrategy.selectAgent(quotes, scoringEngine, null);

        assertNotNull(selected);
        assertEquals("cheap-slow", selected.getAgentId());
    }

    @Test
    void testFastestCompletionStrategy() {
        List<AgentQuoteResponse> quotes = List.of(cheapSlowQuote, expensiveFastQuote, highQualityQuote);
        AgentQuoteResponse selected = fastestCompletionStrategy.selectAgent(quotes, scoringEngine, null);

        assertNotNull(selected);
        assertEquals("expensive-fast", selected.getAgentId());
    }

    @Test
    void testHighestQualityStrategy() {
        List<AgentQuoteResponse> quotes = List.of(cheapSlowQuote, expensiveFastQuote, highQualityQuote);
        AgentQuoteResponse selected = highestQualityStrategy.selectAgent(quotes, scoringEngine, null);

        assertNotNull(selected);
        assertEquals("high-quality", selected.getAgentId());
    }

    @Test
    void testLeastLoadedStrategy() {
        List<AgentQuoteResponse> quotes = List.of(cheapSlowQuote, expensiveFastQuote, highQualityQuote);
        AgentQuoteResponse selected = leastLoadedStrategy.selectAgent(quotes, scoringEngine, null);

        assertNotNull(selected);
        assertEquals("high-quality", selected.getAgentId());
    }

    @Test
    void testBalancedStrategy() {
        List<AgentQuoteResponse> quotes = List.of(cheapSlowQuote, expensiveFastQuote, highQualityQuote);
        AgentQuoteResponse selected = balancedStrategy.selectAgent(quotes, scoringEngine, null);

        assertNotNull(selected);
        assertEquals("BALANCED", balancedStrategy.getStrategyName());
    }
}
