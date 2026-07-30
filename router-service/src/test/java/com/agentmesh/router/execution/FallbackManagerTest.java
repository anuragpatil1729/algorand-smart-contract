package com.agentmesh.router.execution;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.quote.AgentSelector;
import com.agentmesh.router.quote.QuoteCollector;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FallbackManagerTest {

    private AgentDiscoveryService discoveryService;
    private QuoteCollector quoteCollector;
    private AgentSelector agentSelector;
    private FallbackManager fallbackManager;

    @BeforeEach
    void setUp() {
        discoveryService = mock(AgentDiscoveryService.class);
        quoteCollector = mock(QuoteCollector.class);
        agentSelector = mock(AgentSelector.class);
        fallbackManager = new FallbackManager(discoveryService, quoteCollector, agentSelector);
    }

    @Test
    void testFindFallbackAssignmentFromPrecollectedAlternatives() {
        TaskAssignment assignment = new TaskAssignment();
        assignment.setTaskId("t1");
        assignment.setRequiredCapability("CODING");
        assignment.setSelectedAgentId("agent-failed-1");

        AgentQuoteResponse altQuote = new AgentQuoteResponse();
        altQuote.setAgentId("agent-alt-2");
        altQuote.setAgentName("Alt Agent");
        altQuote.setQuotedPrice(50.0);
        altQuote.setEstimatedDuration(10);
        altQuote.setValid(true);

        assignment.setAlternativeAgents(List.of(altQuote));

        AssignmentPlan plan = new AssignmentPlan("wf-fb", "BALANCED");
        plan.addAssignment(assignment);
        ExecutionContext context = new ExecutionContext("wf-fb", plan);

        Set<String> failedAgents = new HashSet<>();

        Optional<TaskAssignment> fallbackOpt = fallbackManager.findFallbackAssignment(assignment, context, failedAgents);

        assertTrue(fallbackOpt.isPresent());
        assertEquals("agent-alt-2", fallbackOpt.get().getSelectedAgentId());
        assertTrue(fallbackOpt.get().getSelectionReason().contains("pre-collected"));
    }
}
