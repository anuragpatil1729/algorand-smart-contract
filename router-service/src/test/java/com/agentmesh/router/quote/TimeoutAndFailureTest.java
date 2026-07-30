package com.agentmesh.router.quote;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TimeoutAndFailureTest {

    private AgentDiscoveryService discoveryService;
    private RestTemplate restTemplate;
    private QuoteCollector quoteCollector;

    @BeforeEach
    void setUp() {
        discoveryService = mock(AgentDiscoveryService.class);
        restTemplate = mock(RestTemplate.class);
        QuoteRequestBuilder requestBuilder = new QuoteRequestBuilder();
        QuoteResponseParser responseParser = new QuoteResponseParser();
        QuoteValidator validator = new QuoteValidator();
        QuoteCache cache = new QuoteCache(300);

        quoteCollector = new QuoteCollector(
                discoveryService, requestBuilder, responseParser, validator, cache, restTemplate, 200, 0
        );
    }

    @Test
    void testAgentUnreachableFallbackGracefulFailure() {
        PlannedTaskDto task = new PlannedTaskDto();
        task.setTaskId("task-offline");
        task.setRequiredCapability("QA");
        task.setComplexity("MEDIUM");

        Agent offlineAgent = Agent.builder()
                .id("agent-offline-01")
                .name("Offline Agent")
                .endpoint("http://localhost:9999")
                .basePrice(50.0)
                .rating(4.5)
                .build();

        when(discoveryService.findAgentsByCapability("QA")).thenReturn(List.of(offlineAgent));
        when(restTemplate.postForObject(anyString(), any(), eq(java.util.Map.class)))
                .thenThrow(new RestClientException("Connection refused"));

        List<AgentQuoteResponse> quotes = quoteCollector.collectQuotesForTask(task, "wf-offline");

        // Single failing/offline agent should NOT stop quote collection or throw uncaught exception.
        // It should produce a dynamic fallback quote.
        assertNotNull(quotes);
        assertEquals(1, quotes.size());
        AgentQuoteResponse fallbackQuote = quotes.get(0);
        assertEquals("agent-offline-01", fallbackQuote.getAgentId());
        assertEquals("FALLBACK_ESTIMATED", fallbackQuote.getStatus());
        assertTrue(fallbackQuote.getQuotedPrice() > 0.0);
    }

    @Test
    void testPartialFailureOneAgentFailsOneSucceeds() {
        PlannedTaskDto task = new PlannedTaskDto();
        task.setTaskId("task-partial");
        task.setRequiredCapability("DEVELOPMENT");

        Agent agent1 = Agent.builder().id("a1").name("Slow Agent").endpoint("http://localhost:8001").basePrice(100.0).build();
        Agent agent2 = Agent.builder().id("a2").name("Fast Agent").endpoint("http://localhost:8002").basePrice(60.0).build();

        when(discoveryService.findAgentsByCapability("DEVELOPMENT")).thenReturn(List.of(agent1, agent2));

        when(restTemplate.postForObject(eq("http://localhost:8001/quote"), any(), eq(java.util.Map.class)))
                .thenThrow(new RestClientException("Timeout exception"));

        when(restTemplate.postForObject(eq("http://localhost:8002/quote"), any(), eq(java.util.Map.class)))
                .thenReturn(java.util.Map.of("agentId", "a2", "price", 60.0, "estimatedTime", 5, "confidence", 99.0));

        List<AgentQuoteResponse> quotes = quoteCollector.collectQuotesForTask(task, "wf-partial");

        assertEquals(2, quotes.size());
        assertTrue(quotes.stream().anyMatch(q -> "a2".equals(q.getAgentId()) && Double.valueOf(60.0).equals(q.getQuotedPrice())));
        assertTrue(quotes.stream().anyMatch(q -> "a1".equals(q.getAgentId()) && "FALLBACK_ESTIMATED".equals(q.getStatus())));
    }
}
