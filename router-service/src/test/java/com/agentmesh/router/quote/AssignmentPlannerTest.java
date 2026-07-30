package com.agentmesh.router.quote;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskAssignment;
import com.agentmesh.router.quote.strategy.BalancedStrategy;
import com.agentmesh.router.quote.strategy.LowestPriceStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssignmentPlannerTest {

    private QuoteCollector quoteCollector;
    private AgentSelector agentSelector;
    private AssignmentPlanner assignmentPlanner;
    private AgentDiscoveryService discoveryService;

    @BeforeEach
    void setUp() {
        discoveryService = mock(AgentDiscoveryService.class);
        QuoteRequestBuilder requestBuilder = new QuoteRequestBuilder();
        QuoteResponseParser responseParser = new QuoteResponseParser();
        QuoteValidator validator = new QuoteValidator();
        QuoteCache cache = new QuoteCache(300);
        RestTemplate restTemplate = mock(RestTemplate.class);

        quoteCollector = new QuoteCollector(
                discoveryService, requestBuilder, responseParser, validator, cache, restTemplate, 1000, 0
        );

        ScoringEngine scoringEngine = new ScoringEngine();
        BalancedStrategy balancedStrategy = new BalancedStrategy();
        LowestPriceStrategy lowestPriceStrategy = new LowestPriceStrategy();

        agentSelector = new AgentSelector(
                scoringEngine, discoveryService, List.of(balancedStrategy, lowestPriceStrategy), balancedStrategy
        );

        assignmentPlanner = new AssignmentPlanner(quoteCollector, agentSelector);
    }

    @Test
    void testGenerateAssignmentPlan() {
        PlannedTaskDto task1 = new PlannedTaskDto();
        task1.setTaskId("task-01");
        task1.setTaskName("Market Research");
        task1.setRequiredCapability("RESEARCH");
        task1.setComplexity("MEDIUM");

        WorkflowPlanResponseDto workflowPlan = new WorkflowPlanResponseDto();
        workflowPlan.setWorkflowId("wf-test-01");
        workflowPlan.setTaskList(List.of(task1));

        Agent agent1 = Agent.builder().id("agent-research-01").name("Research Agent").basePrice(45.0).rating(4.9).build();
        when(discoveryService.findAgentsByCapability("RESEARCH")).thenReturn(List.of(agent1));
        when(discoveryService.discoverAllAgents()).thenReturn(List.of(agent1));

        AssignmentPlan plan = assignmentPlanner.generateAssignmentPlan(workflowPlan, "BALANCED", null);

        assertNotNull(plan);
        assertEquals("wf-test-01", plan.getWorkflowId());
        assertEquals("BALANCED", plan.getSelectionStrategyUsed());
        assertEquals(1, plan.getAssignments().size());

        TaskAssignment assignment = plan.getAssignments().get(0);
        assertEquals("task-01", assignment.getTaskId());
        assertEquals("agent-research-01", assignment.getSelectedAgentId());
        assertNotNull(assignment.getSelectionReason());
        assertTrue(plan.getTotalQuotedPrice() > 0.0);
    }
}
