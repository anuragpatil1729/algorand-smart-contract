package com.agentmesh.router.controller;

import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import com.agentmesh.router.quote.AssignmentPlanner;
import com.agentmesh.router.quote.QuoteAggregator;
import com.agentmesh.router.quote.QuoteCollector;
import com.agentmesh.router.quote.ScoringEngine;
import com.agentmesh.router.quote.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping
@Tag(name = "Quote & Selection Controller", description = "Endpoints for AI agent quotation collection, scoring, and multi-criteria assignment planning")
public class QuoteController {

    private final QuoteCollector quoteCollector;
    private final QuoteAggregator quoteAggregator;
    private final AssignmentPlanner assignmentPlanner;
    private final ScoringEngine scoringEngine;

    public QuoteController(
            QuoteCollector quoteCollector,
            QuoteAggregator quoteAggregator,
            AssignmentPlanner assignmentPlanner,
            ScoringEngine scoringEngine
    ) {
        this.quoteCollector = quoteCollector;
        this.quoteAggregator = quoteAggregator;
        this.assignmentPlanner = assignmentPlanner;
        this.scoringEngine = scoringEngine;
    }

    @PostMapping("/api/quotes/collect")
    @Operation(summary = "Collect quotations from candidate agents for a WorkflowPlan")
    public ResponseEntity<ApiResponse<QuoteCollectionResponse>> collectQuotes(@RequestBody QuoteCollectionRequest request) {
        if (request == null || request.getWorkflowPlan() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("WorkflowPlan must be provided"));
        }

        WorkflowPlanResponseDto plan = request.getWorkflowPlan();
        String workflowId = request.getWorkflowId() != null ? request.getWorkflowId() :
                (plan.getWorkflowId() != null ? plan.getWorkflowId() : UUID.randomUUID().toString());
        plan.setWorkflowId(workflowId);

        Map<String, List<AgentQuoteResponse>> rawQuotesMap = quoteCollector.collectQuotesForWorkflow(plan);
        List<TaskQuoteSummary> summaries = quoteAggregator.aggregateWorkflowQuotes(plan.getTaskList(), rawQuotesMap, null);

        assignmentPlanner.storeQuotes(workflowId, summaries);

        int totalQuotesCount = rawQuotesMap.values().stream().mapToInt(List::size).sum();
        int taskCount = plan.getTaskList() != null ? plan.getTaskList().size() : 0;

        QuoteCollectionResponse response = new QuoteCollectionResponse(workflowId, taskCount, totalQuotesCount, summaries);
        return ResponseEntity.ok(ApiResponse.success("Quotes collected successfully", response));
    }

    @PostMapping("/api/quotes/select")
    @Operation(summary = "Generate an AssignmentPlan for a WorkflowPlan using a dynamic selection strategy")
    public ResponseEntity<ApiResponse<AssignmentPlan>> selectAgents(@RequestBody QuoteSelectionRequest request) {
        if (request == null || request.getWorkflowPlan() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("WorkflowPlan must be provided"));
        }

        WorkflowPlanResponseDto plan = request.getWorkflowPlan();
        String workflowId = request.getWorkflowId() != null ? request.getWorkflowId() :
                (plan.getWorkflowId() != null ? plan.getWorkflowId() : UUID.randomUUID().toString());
        plan.setWorkflowId(workflowId);

        String strategy = request.getStrategy() != null ? request.getStrategy() : "BALANCED";
        AssignmentPlan assignmentPlan = assignmentPlanner.generateAssignmentPlan(plan, strategy, request.getCustomWeights());

        return ResponseEntity.ok(ApiResponse.success("Assignment plan generated successfully", assignmentPlan));
    }

    @GetMapping("/api/quotes/{workflowId}")
    @Operation(summary = "Retrieve collected quotes for a workflow ID")
    public ResponseEntity<ApiResponse<List<TaskQuoteSummary>>> getCollectedQuotes(@PathVariable String workflowId) {
        List<TaskQuoteSummary> summaries = assignmentPlanner.getStoredQuotes(workflowId);
        if (summaries == null) {
            summaries = Collections.emptyList();
        }
        return ResponseEntity.ok(ApiResponse.success("Collected quotes retrieved successfully", summaries));
    }

    @GetMapping("/api/quotes/workflow/{workflowId}")
    @Operation(summary = "Get all agent quotes for tasks in a workflow (legacy path)")
    public ResponseEntity<ApiResponse<List<TaskQuoteSummary>>> getQuotesForWorkflowLegacy(@PathVariable String workflowId) {
        return getCollectedQuotes(workflowId);
    }

    @GetMapping("/api/assignments/{workflowId}")
    @Operation(summary = "Retrieve generated AssignmentPlan for a workflow ID")
    public ResponseEntity<ApiResponse<AssignmentPlan>> getAssignmentPlan(@PathVariable String workflowId) {
        AssignmentPlan plan = assignmentPlanner.getStoredAssignmentPlan(workflowId);
        if (plan == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("Assignment plan not found for workflowId: " + workflowId));
        }
        return ResponseEntity.ok(ApiResponse.success("Assignment plan retrieved successfully", plan));
    }
}
