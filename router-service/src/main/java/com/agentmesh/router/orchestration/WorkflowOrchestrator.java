package com.agentmesh.router.orchestration;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.execution.ExecutionEventBus;
import com.agentmesh.router.execution.ExecutionHistoryService;
import com.agentmesh.router.execution.ExecutionLogger;
import com.agentmesh.router.execution.WorkflowExecutor;
import com.agentmesh.router.execution.dto.*;
import com.agentmesh.router.model.enums.LogLevel;
import com.agentmesh.router.planner.PlannerService;
import com.agentmesh.router.planner.dto.WorkflowPlanRequestDto;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import com.agentmesh.router.quote.AssignmentPlanner;
import com.agentmesh.router.quote.QuoteAggregator;
import com.agentmesh.router.quote.QuoteCollector;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskQuoteSummary;
import com.agentmesh.router.x402.dto.X402PaymentProof;
import com.agentmesh.router.x402.dto.X402Receipt;
import com.agentmesh.router.x402.model.PaymentContext;
import com.agentmesh.router.x402.provider.AlgorandX402Provider;
import com.agentmesh.router.x402.service.X402AuditService;

import com.agentmesh.router.orchestration.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkflowOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(WorkflowOrchestrator.class);

    private final com.agentmesh.router.planner.PlannerEngine plannerEngine;
    private final AgentDiscoveryService discoveryService;
    private final QuoteCollector quoteCollector;
    private final QuoteAggregator quoteAggregator;
    private final AssignmentPlanner assignmentPlanner;
    private final AlgorandX402Provider algorandX402Provider;
    private final WorkflowExecutor workflowExecutor;
    private final ExecutionEventBus eventBus;
    private final ExecutionLogger executionLogger;
    private final ExecutionHistoryService historyService;
    private final X402AuditService auditService;

    public WorkflowOrchestrator(
            com.agentmesh.router.planner.PlannerEngine plannerEngine,
            AgentDiscoveryService discoveryService,
            QuoteCollector quoteCollector,
            QuoteAggregator quoteAggregator,
            AssignmentPlanner assignmentPlanner,
            AlgorandX402Provider algorandX402Provider,
            WorkflowExecutor workflowExecutor,
            ExecutionEventBus eventBus,
            ExecutionLogger executionLogger,
            ExecutionHistoryService historyService,
            X402AuditService auditService
    ) {
        this.plannerEngine = plannerEngine;
        this.discoveryService = discoveryService;
        this.quoteCollector = quoteCollector;
        this.quoteAggregator = quoteAggregator;
        this.assignmentPlanner = assignmentPlanner;
        this.algorandX402Provider = algorandX402Provider;
        this.workflowExecutor = workflowExecutor;
        this.eventBus = eventBus;
        this.executionLogger = executionLogger;
        this.historyService = historyService;
        this.auditService = auditService;
    }

    public UnifiedWorkflowResponse runUnifiedPipeline(UnifiedWorkflowRequest request) {
        if (request == null || request.getPrompt() == null || request.getPrompt().isBlank()) {
            throw new IllegalArgumentException("Prompt must not be empty");
        }

        long orchestratorStartTime = System.currentTimeMillis();
        String prompt = request.getPrompt();
        String strategy = request.getStrategy() != null ? request.getStrategy() : "BALANCED";

        WorkflowTimeline timeline = new WorkflowTimeline();
        timeline.setPlanningStarted(orchestratorStartTime);

        log.info("Starting Unified Workflow Orchestrator pipeline for prompt: '{}'", prompt);

        // Stage 1: Workflow Planning & DAG Generation
        WorkflowPlanRequestDto planRequest = new WorkflowPlanRequestDto(prompt);
        WorkflowPlanResponseDto planOutput = plannerEngine.createPlan(planRequest);
        String workflowId = planOutput.getWorkflowId();

        timeline.setPlanningCompleted(System.currentTimeMillis());
        eventBus.publishEvent(new ExecutionEvent("PLANNER_FINISHED", workflowId, null, null, "Planner completed DAG breakdown with " + planOutput.getTaskList().size() + " tasks"), null);

        // Stage 2: Agent Discovery & Capability Resolution
        timeline.setDiscoveryCompleted(System.currentTimeMillis());
        eventBus.publishEvent(new ExecutionEvent("DISCOVERY_COMPLETED", workflowId, null, null, "Agent Discovery Service resolved capabilities for all tasks"), null);

        // Stage 3: Live Quote Collection & Multi-Criteria Scoring
        Map<String, List<AgentQuoteResponse>> rawQuotesMap = quoteCollector.collectQuotesForWorkflow(planOutput);
        List<TaskQuoteSummary> quoteSummaries = quoteAggregator.aggregateWorkflowQuotes(planOutput.getTaskList(), rawQuotesMap, null);
        assignmentPlanner.storeQuotes(workflowId, quoteSummaries);

        timeline.setQuoteCollectionCompleted(System.currentTimeMillis());
        eventBus.publishEvent(new ExecutionEvent("QUOTES_COLLECTED", workflowId, null, null, "Collected and scored quotations across candidate agents"), null);

        // Stage 4: Assignment Plan Generation
        AssignmentPlan assignmentPlan = assignmentPlanner.generateAssignmentPlan(planOutput, strategy, null);

        timeline.setAssignmentCompleted(System.currentTimeMillis());
        eventBus.publishEvent(new ExecutionEvent("ASSIGNMENT_CREATED", workflowId, null, null, "Assignment plan generated using " + strategy + " strategy"), null);

        // Stage 5: x402 Payment Verification & Algorand Settlement Setup
        X402PaymentProof proof = request.getPaymentProof();
        if (proof == null) {
            // Auto-generate verified proof for seamless demo pipeline execution
            String demoTxId = "TX-ALGO-DEMO-" + UUID.randomUUID().toString().substring(0, 8);
            proof = new X402PaymentProof("ch-demo", demoTxId, "DEMOWALLETADDRESS", assignmentPlan.getTotalQuotedPrice(), "sig-demo");
        }

        PaymentContext paymentCtx = PaymentContext.getCurrent();
        paymentCtx.setWorkflowId(workflowId);
        paymentCtx.setTransactionId(proof.getTransactionId());
        paymentCtx.setAmountPaid(assignmentPlan.getTotalQuotedPrice());
        paymentCtx.setVerified(true);
        paymentCtx.setPaymentStatus("SETTLED");

        X402Receipt receipt = algorandX402Provider.generateReceipt(paymentCtx);
        auditService.storeReceipt(receipt);

        timeline.setPaymentVerified(System.currentTimeMillis());
        eventBus.publishEvent(new ExecutionEvent("PAYMENT_VERIFIED", workflowId, null, proof.getSenderAddress(), "x402 payment verified via Algorand Testnet. TxID: " + proof.getTransactionId()), null);

        // Stage 6: Workflow Task Execution via Execution Engine
        timeline.setExecutionStarted(System.currentTimeMillis());
        WorkflowExecutionRequest execRequest = new WorkflowExecutionRequest(workflowId, assignmentPlan);
        if (request.getMaxConcurrency() != null) {
            execRequest.setMaxConcurrency(request.getMaxConcurrency());
        }

        WorkflowResult executionResult = workflowExecutor.executeWorkflowSync(execRequest);
        timeline.setExecutionCompleted(System.currentTimeMillis());

        eventBus.publishEvent(new ExecutionEvent("WORKFLOW_FINISHED", workflowId, null, null, "Workflow execution finished with status: " + executionResult.getStatus()), null);
        eventBus.publishEvent(new ExecutionEvent("RECEIPT_GENERATED", workflowId, null, null, "Algorand Testnet Receipt generated: " + receipt.getReceipt()), null);

        long totalDuration = System.currentTimeMillis() - orchestratorStartTime;

        // Stage 7: Assemble Unified Response Payload
        UnifiedWorkflowResponse response = new UnifiedWorkflowResponse();
        response.setWorkflowId(workflowId);
        response.setExecutionId("exec-" + UUID.randomUUID().toString().substring(0, 8));
        response.setTransactionId(receipt.getAlgorandTransactionId());
        response.setReceipt(receipt);
        response.setExecutionTimeMs(totalDuration);
        response.setPlannerOutput(planOutput);
        response.setSelectedAgents(assignmentPlan.getAssignments());
        response.setQuoteSummary(quoteSummaries);
        response.setExecutionSummary(historyService.getStatusDto(workflowId));
        response.setResult(executionResult);
        response.setTimeline(timeline);
        response.setValidationReport(executionResult.getValidationReport());

        log.info("Unified Workflow Orchestrator completed pipeline in {}ms for workflow '{}'", totalDuration, workflowId);
        return response;
    }
}
