package com.agentmesh.router.quote;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class QuoteCollectorTest {

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
                discoveryService, requestBuilder, responseParser, validator, cache, restTemplate, 1000, 1
        );
    }

    @Test
    void testCollectQuotesSuccessFromMockAgent() {
        PlannedTaskDto task = new PlannedTaskDto();
        task.setTaskId("t1");
        task.setTaskName("Task 1");
        task.setRequiredCapability("CODING");

        Agent agent = Agent.builder().id("a1").name("Coding Agent").endpoint("http://localhost:8002").basePrice(80.0).build();
        when(discoveryService.findAgentsByCapability("CODING")).thenReturn(List.of(agent));

        Map<String, Object> mockResponse = Map.of(
                "agentId", "a1",
                "agentName", "Coding Agent",
                "price", 75.0,
                "estimatedTime", 12,
                "confidence", 98.0,
                "reputation", 4.9,
                "healthScore", 100.0
        );
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(mockResponse);

        List<AgentQuoteResponse> quotes = quoteCollector.collectQuotesForTask(task, "wf-1");

        assertFalse(quotes.isEmpty());
        assertEquals(1, quotes.size());
        assertEquals("a1", quotes.get(0).getAgentId());
        assertEquals(75.0, quotes.get(0).getQuotedPrice());
    }

    @Test
    void testCollectQuotesForWorkflow() {
        PlannedTaskDto task = new PlannedTaskDto();
        task.setTaskId("t1");
        task.setRequiredCapability("RESEARCH");

        WorkflowPlanResponseDto plan = new WorkflowPlanResponseDto();
        plan.setWorkflowId("wf-100");
        plan.setTaskList(List.of(task));

        Agent agent = Agent.builder().id("r1").name("Research Agent").endpoint("http://localhost:8001").basePrice(40.0).build();
        when(discoveryService.findAgentsByCapability("RESEARCH")).thenReturn(List.of(agent));

        Map<String, List<AgentQuoteResponse>> result = quoteCollector.collectQuotesForWorkflow(plan);

        assertNotNull(result);
        assertTrue(result.containsKey("t1"));
    }
}
