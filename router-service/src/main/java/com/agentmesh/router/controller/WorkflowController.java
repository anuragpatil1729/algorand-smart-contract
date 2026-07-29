package com.agentmesh.router.controller;

import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.dto.WorkflowDto;
import com.agentmesh.router.dto.WorkflowRequestDto;
import com.agentmesh.router.service.AgentMeshService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workflows")
@Tag(name = "Workflow Controller", description = "Endpoints for orchestration workflow submissions, approvals, and DAG tracking")
public class WorkflowController {

    private final AgentMeshService agentMeshService;

    public WorkflowController(AgentMeshService agentMeshService) {
        this.agentMeshService = agentMeshService;
    }

    @PostMapping
    @Operation(summary = "Submit a prompt to decompose and create a new workflow")
    public ResponseEntity<ApiResponse<WorkflowDto>> createWorkflow(@Valid @RequestBody WorkflowRequestDto request) {
        WorkflowDto result = agentMeshService.createWorkflow(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Workflow created and decomposed successfully", result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve workflow details and task DAG state by ID")
    public ResponseEntity<ApiResponse<WorkflowDto>> getWorkflowById(@PathVariable String id) {
        WorkflowDto result = agentMeshService.getWorkflowById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Workflow not found with ID: " + id));
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping
    @Operation(summary = "List all workflows")
    public ResponseEntity<ApiResponse<List<WorkflowDto>>> getAllWorkflows() {
        List<WorkflowDto> results = agentMeshService.getAllWorkflows();
        return ResponseEntity.ok(ApiResponse.success("Workflows retrieved successfully", results));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve quotes and trigger execution with Algorand Escrow lock")
    public ResponseEntity<ApiResponse<WorkflowDto>> approveWorkflow(@PathVariable String id) {
        WorkflowDto result = agentMeshService.approveAndExecute(id);
        return ResponseEntity.ok(ApiResponse.success("Workflow approved and Algorand Escrow locked", result));
    }

    @PostMapping("/{id}/execute")
    @Operation(summary = "Execute workflow with Algorand Escrow lock")
    public ResponseEntity<ApiResponse<WorkflowDto>> executeWorkflow(@PathVariable String id) {
        WorkflowDto result = agentMeshService.approveAndExecute(id);
        return ResponseEntity.ok(ApiResponse.success("Workflow execution initiated", result));
    }
}
