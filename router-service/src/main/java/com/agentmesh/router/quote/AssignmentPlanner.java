package com.agentmesh.router.quote;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import com.agentmesh.router.quote.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AssignmentPlanner {

    private static final Logger log = LoggerFactory.getLogger(AssignmentPlanner.class);

    private final QuoteCollector quoteCollector;
    private final AgentSelector agentSelector;
    private final Map<String, AssignmentPlan> storedPlans = new ConcurrentHashMap<>();
    private final Map<String, List<TaskQuoteSummary>> storedQuotes = new ConcurrentHashMap<>();

    public AssignmentPlanner(QuoteCollector quoteCollector, AgentSelector agentSelector) {
        this.quoteCollector = quoteCollector;
        this.agentSelector = agentSelector;
    }

    public AssignmentPlan generateAssignmentPlan(WorkflowPlanResponseDto workflowPlan, String strategyName, Map<String, Double> customWeightsMap) {
        if (workflowPlan == null || workflowPlan.getTaskList() == null) {
            return new AssignmentPlan("empty", strategyName);
        }

        String workflowId = workflowPlan.getWorkflowId() != null ? workflowPlan.getWorkflowId() : UUID.randomUUID().toString();
        log.info("Generating AssignmentPlan for workflow '{}' with strategy '{}'", workflowId, strategyName);

        // Step 1: Collect live quotes from candidate agents for all tasks in workflow
        Map<String, List<AgentQuoteResponse>> taskQuotesMap = quoteCollector.collectQuotesForWorkflow(workflowPlan);

        // Convert custom weights if provided
        ScoringEngine.Weights weights = customWeightsMap != null ?
                new ScoringEngine().parseWeightsMap(customWeightsMap) : null;

        AssignmentPlan plan = new AssignmentPlan(workflowId, strategyName != null ? strategyName : "BALANCED");

        // Step 2: For each Planned Task, choose best agent using selected strategy
        for (PlannedTaskDto task : workflowPlan.getTaskList()) {
            List<AgentQuoteResponse> quotes = taskQuotesMap.getOrDefault(task.getTaskId(), Collections.emptyList());
            TaskAssignment assignment = agentSelector.selectAgentForTask(task, quotes, strategyName, weights);
            plan.addAssignment(assignment);
        }

        storedPlans.put(workflowId, plan);
        return plan;
    }

    public void storeQuotes(String workflowId, List<TaskQuoteSummary> summaries) {
        if (workflowId != null && summaries != null) {
            storedQuotes.put(workflowId, summaries);
        }
    }

    public List<TaskQuoteSummary> getStoredQuotes(String workflowId) {
        return storedQuotes.get(workflowId);
    }

    public AssignmentPlan getStoredAssignmentPlan(String workflowId) {
        return storedPlans.get(workflowId);
    }
}
