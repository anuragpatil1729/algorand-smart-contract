package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.planner.dto.WorkflowPlanRequestDto;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class PlannerEngine implements WorkflowPlanner {

    private static final Logger log = LoggerFactory.getLogger(PlannerEngine.class);

    private final PlanningRulesEngine rulesEngine;
    private final TaskValidator validator;
    private final WorkflowOptimizer optimizer;
    private final TaskDependencyResolver dependencyResolver;
    private final WorkflowGraphBuilder graphBuilder;

    private final Map<String, WorkflowPlanResponseDto> planStorage = new ConcurrentHashMap<>();

    public PlannerEngine(
            PlanningRulesEngine rulesEngine,
            TaskValidator validator,
            WorkflowOptimizer optimizer,
            TaskDependencyResolver dependencyResolver,
            WorkflowGraphBuilder graphBuilder
    ) {
        this.rulesEngine = rulesEngine;
        this.validator = validator;
        this.optimizer = optimizer;
        this.dependencyResolver = dependencyResolver;
        this.graphBuilder = graphBuilder;
    }

    @Override
    public WorkflowPlanResponseDto createPlan(WorkflowPlanRequestDto request) {
        if (request == null || request.getPrompt() == null || request.getPrompt().isBlank()) {
            throw new IllegalArgumentException("Workflow plan request prompt cannot be empty");
        }

        String workflowId = "wf-plan-" + UUID.randomUUID().toString().substring(0, 8);
        log.info("PlannerEngine: Generating workflow plan {} for prompt: '{}'", workflowId, request.getPrompt());

        List<PlannedTaskDto> rawTasks = rulesEngine.generateTasks(request.getPrompt());

        List<String> warnings = validator.validateTasks(rawTasks);

        Map<Integer, List<PlannedTaskDto>> stages = optimizer.optimizeExecutionStages(rawTasks);
        List<List<String>> parallelGroups = optimizer.extractParallelGroups(stages);

        int criticalPathDuration = dependencyResolver.calculateCriticalPathDurationSeconds(rawTasks);

        double totalCost = rawTasks.stream()
                .mapToDouble(t -> t.getEstimatedCost() != null ? t.getEstimatedCost() : 50.0)
                .sum();

        List<String> reqCaps = rawTasks.stream()
                .map(PlannedTaskDto::getRequiredCapability)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Object> graphRep = graphBuilder.buildGraphRepresentation(rawTasks);

        WorkflowPlanResponseDto response = new WorkflowPlanResponseDto();
        response.setWorkflowId(workflowId);
        response.setPrompt(request.getPrompt());
        response.setTaskList(rawTasks);
        response.setExecutionStages(stages);
        response.setParallelGroups(parallelGroups);
        response.setTotalEstimatedDurationSeconds(criticalPathDuration);
        response.setTotalEstimatedCost(Math.round(totalCost * 100.0) / 100.0);
        response.setRequiredCapabilities(reqCaps);
        response.setGraphRepresentation(graphRep);
        response.setWarnings(warnings);
        response.setMissingCapabilities(Collections.emptyList());

        planStorage.put(workflowId, response);
        return response;
    }

    @Override
    public WorkflowPlanResponseDto getPlan(String workflowId) {
        return planStorage.get(workflowId);
    }

    @Override
    public List<String> validatePlan(WorkflowPlanResponseDto plan) {
        if (plan == null || plan.getTaskList() == null) {
            return List.of("Invalid plan: empty task list");
        }
        return validator.validateTasks(plan.getTaskList());
    }

    @Override
    public WorkflowPlanResponseDto optimizePlan(WorkflowPlanResponseDto plan) {
        if (plan == null || plan.getTaskList() == null) return plan;

        Map<Integer, List<PlannedTaskDto>> stages = optimizer.optimizeExecutionStages(plan.getTaskList());
        List<List<String>> parallelGroups = optimizer.extractParallelGroups(stages);
        int duration = dependencyResolver.calculateCriticalPathDurationSeconds(plan.getTaskList());

        plan.setExecutionStages(stages);
        plan.setParallelGroups(parallelGroups);
        plan.setTotalEstimatedDurationSeconds(duration);
        return plan;
    }
}
