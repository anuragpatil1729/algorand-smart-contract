package com.agentmesh.router.controller;

import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.planner.WorkflowPlanner;
import com.agentmesh.router.planner.dto.WorkflowPlanRequestDto;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planner")
@Tag(name = "Workflow Planner Controller", description = "Endpoints for generating, validating, and optimizing AI workflow DAG plans")
public class PlannerController {

    private final WorkflowPlanner workflowPlanner;

    public PlannerController(WorkflowPlanner workflowPlanner) {
        this.workflowPlanner = workflowPlanner;
    }

    @PostMapping("/plan")
    @Operation(summary = "Generate an execution-ready workflow DAG plan from natural language prompt")
    public ResponseEntity<ApiResponse<WorkflowPlanResponseDto>> createPlan(@RequestBody WorkflowPlanRequestDto request) {
        WorkflowPlanResponseDto plan = workflowPlanner.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Workflow plan generated successfully", plan));
    }

    @GetMapping("/{workflowId}")
    @Operation(summary = "Retrieve a generated workflow plan by ID")
    public ResponseEntity<ApiResponse<WorkflowPlanResponseDto>> getPlan(@PathVariable String workflowId) {
        WorkflowPlanResponseDto plan = workflowPlanner.getPlan(workflowId);
        if (plan != null) {
            return ResponseEntity.ok(ApiResponse.success("Workflow plan retrieved", plan));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Workflow plan not found with ID: " + workflowId));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate a workflow plan DAG graph for cycles and integrity")
    public ResponseEntity<ApiResponse<List<String>>> validatePlan(@RequestBody WorkflowPlanResponseDto plan) {
        List<String> errors = workflowPlanner.validatePlan(plan);
        if (errors.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("Workflow plan DAG is valid", errors));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Workflow plan validation errors detected", errors));
    }

    @PostMapping("/optimize")
    @Operation(summary = "Optimize workflow task execution into parallel stages")
    public ResponseEntity<ApiResponse<WorkflowPlanResponseDto>> optimizePlan(@RequestBody WorkflowPlanResponseDto plan) {
        WorkflowPlanResponseDto optimized = workflowPlanner.optimizePlan(plan);
        return ResponseEntity.ok(ApiResponse.success("Workflow plan optimized into parallel stages", optimized));
    }
}
