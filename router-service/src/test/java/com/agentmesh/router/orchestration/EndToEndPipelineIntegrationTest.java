package com.agentmesh.router.orchestration;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.execution.*;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.orchestration.dto.UnifiedWorkflowRequest;
import com.agentmesh.router.orchestration.dto.UnifiedWorkflowResponse;
import com.agentmesh.router.planner.*;
import com.agentmesh.router.quote.AgentSelector;
import com.agentmesh.router.quote.AssignmentPlanner;
import com.agentmesh.router.quote.QuoteAggregator;
import com.agentmesh.router.quote.QuoteCollector;
import com.agentmesh.router.quote.QuoteRequestBuilder;
import com.agentmesh.router.quote.QuoteResponseParser;
import com.agentmesh.router.quote.QuoteValidator;
import com.agentmesh.router.quote.QuoteCache;
import com.agentmesh.router.quote.ScoringEngine;
import com.agentmesh.router.quote.strategy.BalancedStrategy;
import com.agentmesh.router.x402.provider.AlgorandX402Provider;
import com.agentmesh.router.x402.service.X402AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EndToEndPipelineIntegrationTest {

    private WorkflowOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        // Planner components
        CapabilityResolver capabilityResolver = new CapabilityResolver();
        TaskComplexityEstimator estimator = new TaskComplexityEstimator();
        PlanningRulesEngine rulesEngine = new PlanningRulesEngine(capabilityResolver, estimator);
        TaskDependencyResolver dependencyResolver = new TaskDependencyResolver();
        TaskValidator taskValidator = new TaskValidator(dependencyResolver);
        WorkflowOptimizer optimizer = new WorkflowOptimizer(dependencyResolver);
        WorkflowGraphBuilder graphBuilder = new WorkflowGraphBuilder();
        PlannerEngine plannerEngine = new PlannerEngine(rulesEngine, taskValidator, optimizer, dependencyResolver, graphBuilder);

        // Discovery & RestTemplate
        RestTemplate restTemplate = mock(RestTemplate.class);
        AgentDiscoveryService discoveryService = mock(AgentDiscoveryService.class);
        Agent researchAgent = Agent.builder().id("agent-research-01").name("Research Agent").basePrice(45.0).rating(4.9).build();
        Agent codeAgent = Agent.builder().id("agent-code-02").name("Code Agent").basePrice(80.0).rating(4.8).build();

        when(discoveryService.discoverAllAgents()).thenReturn(List.of(researchAgent, codeAgent));
        when(discoveryService.findAgentsByCapability(anyString())).thenReturn(List.of(researchAgent, codeAgent));

        // Quote & Assignment components
        QuoteRequestBuilder requestBuilder = new QuoteRequestBuilder();
        QuoteResponseParser responseParser = new QuoteResponseParser();
        QuoteValidator quoteValidator = new QuoteValidator();
        QuoteCache quoteCache = new QuoteCache(300);
        QuoteCollector quoteCollector = new QuoteCollector(discoveryService, requestBuilder, responseParser, quoteValidator, quoteCache, restTemplate, 1000, 0);

        ScoringEngine scoringEngine = new ScoringEngine();
        BalancedStrategy balancedStrategy = new BalancedStrategy();
        QuoteAggregator quoteAggregator = new QuoteAggregator(scoringEngine);

        AgentSelector agentSelector = new AgentSelector(scoringEngine, discoveryService, List.of(balancedStrategy), balancedStrategy);
        AssignmentPlanner assignmentPlanner = new AssignmentPlanner(quoteCollector, agentSelector);

        // x402 Payment Provider
        AlgorandX402Provider algorandX402Provider = new AlgorandX402Provider(
                "D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ",
                "https://facilitator.goplausible.xyz",
                "31566704",
                restTemplate
        );

        // Execution components
        DependencyScheduler dependencyScheduler = new DependencyScheduler();
        ParallelExecutionEngine parallelEngine = new ParallelExecutionEngine(5);
        TaskExecutor taskExecutor = new TaskExecutor(restTemplate, discoveryService);
        TimeoutManager timeoutManager = new TimeoutManager(5000);
        RetryManager retryManager = new RetryManager(1, 50);
        FallbackManager fallbackManager = new FallbackManager(discoveryService, quoteCollector, agentSelector);

        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        ExecutionEventBus eventBus = new ExecutionEventBus(messagingTemplate);
        ExecutionLogger executionLogger = new ExecutionLogger(null);
        ExecutionStateMachine stateMachine = new ExecutionStateMachine();

        ExecutionCoordinator coordinator = new ExecutionCoordinator(
                dependencyScheduler, parallelEngine, taskExecutor, timeoutManager, retryManager, fallbackManager, eventBus, executionLogger, stateMachine
        );

        ExecutionValidator validator = new ExecutionValidator();
        ExecutionResultAggregator aggregator = new ExecutionResultAggregator();
        ExecutionHistoryService historyService = new ExecutionHistoryService();

        WorkflowExecutor workflowExecutor = new WorkflowExecutor(
                coordinator, stateMachine, eventBus, executionLogger, validator, aggregator, historyService
        );

        X402AuditService auditService = new X402AuditService();

        orchestrator = new WorkflowOrchestrator(
                plannerEngine, discoveryService, quoteCollector, quoteAggregator, assignmentPlanner,
                algorandX402Provider, workflowExecutor, eventBus, executionLogger, historyService, auditService
        );
    }

    @Test
    void testEndToEndPipelineExecution() {
        UnifiedWorkflowRequest request = new UnifiedWorkflowRequest("Create a startup landing page with logo and deployment");

        UnifiedWorkflowResponse response = orchestrator.runUnifiedPipeline(request);

        assertNotNull(response);
        assertNotNull(response.getWorkflowId());
        assertNotNull(response.getExecutionId());
        assertNotNull(response.getTransactionId());
        assertNotNull(response.getReceipt());
        assertTrue(response.getReceipt().getVerified());

        assertNotNull(response.getPlannerOutput());
        assertFalse(response.getPlannerOutput().getTaskList().isEmpty());

        assertNotNull(response.getSelectedAgents());
        assertFalse(response.getSelectedAgents().isEmpty());

        assertNotNull(response.getResult());
        assertEquals("COMPLETED", response.getResult().getStatus());
        assertNotNull(response.getResult().getAggregatedOutput());

        assertNotNull(response.getTimeline());
        assertTrue(response.getTimeline().getExecutionCompleted() >= response.getTimeline().getPlanningStarted());
    }
}
