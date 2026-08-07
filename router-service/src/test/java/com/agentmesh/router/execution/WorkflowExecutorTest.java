package com.agentmesh.router.execution;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.execution.dto.WorkflowExecutionRequest;
import com.agentmesh.router.execution.dto.WorkflowResult;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.quote.AgentSelector;
import com.agentmesh.router.quote.QuoteCollector;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskAssignment;
import com.agentmesh.router.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkflowExecutorTest {

    private WorkflowExecutor workflowExecutor;

    @BeforeEach
    void setUp() {
        DependencyScheduler dependencyScheduler = new DependencyScheduler();
        ParallelExecutionEngine parallelEngine = new ParallelExecutionEngine(5);

        RestTemplate restTemplate = mock(RestTemplate.class);
        AgentDiscoveryService discoveryService = mock(AgentDiscoveryService.class);
        Agent agent1 = Agent.builder().id("a1").name("Agent 1").endpoint("http://localhost:8001").build();
        when(discoveryService.discoverAllAgents()).thenReturn(List.of(agent1));

        TaskExecutor taskExecutor = new TaskExecutor(restTemplate, discoveryService);
        TimeoutManager timeoutManager = new TimeoutManager(5000);
        RetryManager retryManager = new RetryManager(1, 50);

        QuoteCollector quoteCollector = mock(QuoteCollector.class);
        AgentSelector agentSelector = mock(AgentSelector.class);
        FallbackManager fallbackManager = new FallbackManager(discoveryService, quoteCollector, agentSelector);

        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        ExecutionEventBus eventBus = new ExecutionEventBus(messagingTemplate);
        ExecutionLogger executionLogger = new ExecutionLogger(null);
        ExecutionStateMachine stateMachine = new ExecutionStateMachine();

        AgentRepository agentRepository = mock(AgentRepository.class);
        ExecutionCoordinator coordinator = new ExecutionCoordinator(
                dependencyScheduler, parallelEngine, taskExecutor, timeoutManager, retryManager, fallbackManager, eventBus, executionLogger, stateMachine, agentRepository
        );

        ExecutionValidator validator = new ExecutionValidator();
        ExecutionResultAggregator aggregator = new ExecutionResultAggregator();
        ExecutionHistoryService historyService = new ExecutionHistoryService();

        workflowExecutor = new WorkflowExecutor(
                coordinator, stateMachine, eventBus, executionLogger, validator, aggregator, historyService
        );
    }

    @Test
    void testEndToEndWorkflowExecution() {
        TaskAssignment t1 = new TaskAssignment();
        t1.setTaskId("t1");
        t1.setTaskName("Research Task");
        t1.setRequiredCapability("RESEARCH");
        t1.setSelectedAgentId("a1");
        t1.setQuotedPrice(45.0);
        t1.setEstimatedDuration(10);

        AssignmentPlan plan = new AssignmentPlan("wf-exec-1", "BALANCED");
        plan.addAssignment(t1);

        WorkflowExecutionRequest request = new WorkflowExecutionRequest("wf-exec-1", plan);

        WorkflowResult result = workflowExecutor.executeWorkflowSync(request);

        assertNotNull(result);
        assertEquals("wf-exec-1", result.getWorkflowId());
        assertEquals("COMPLETED", result.getStatus());
        assertNotNull(result.getAggregatedOutput());
        assertTrue(result.getTotalExecutionTimeMs() >= 0);
    }
}
