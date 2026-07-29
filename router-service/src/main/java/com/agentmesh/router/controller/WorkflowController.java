package com.agentmesh.router.controller;

import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.dto.WorkflowRequest;
import com.agentmesh.router.dto.WorkflowResponse;
import com.agentmesh.router.model.enums.WorkflowStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflows")
@Tag(name = "Workflow Controller", description = "Endpoints for orchestration workflow submissions, approvals, and DAG tracking")
public class WorkflowController {

    @PostMapping
    @Operation(summary = "Submit a prompt to decompose and create a new workflow")
    public ResponseEntity<ApiResponse<WorkflowResponse>> createWorkflow(@Valid @RequestBody WorkflowRequest request) {
        String id = "wf-" + UUID.randomUUID().toString().substring(0, 8);
        WorkflowResponse placeholder = new WorkflowResponse(
                id,
                request.getPrompt(),
                WorkflowStatus.PENDING_APPROVAL,
                0.0,
                LocalDateTime.now(),
                null,
                new ArrayList<>()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Workflow created successfully", placeholder));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve workflow details and task DAG state by ID")
    public ResponseEntity<ApiResponse<WorkflowResponse>> getWorkflowById(@PathVariable String id) {
        WorkflowResponse placeholder = new WorkflowResponse(
                id,
                "Placeholder Prompt",
                WorkflowStatus.PENDING_APPROVAL,
                0.0,
                LocalDateTime.now(),
                null,
                new ArrayList<>()
        );
        return ResponseEntity.ok(ApiResponse.success(placeholder));
    }

    @GetMapping
    @Operation(summary = "List all workflows")
    public ResponseEntity<ApiResponse<List<WorkflowResponse>>> getAllWorkflows() {
        return ResponseEntity.ok(ApiResponse.success("Workflows retrieved successfully", new ArrayList<>()));
    }

    @PostMapping("/{id}/execute")
    @Operation(summary = "Approve quotes and trigger execution with Algorand Escrow lock")
    public ResponseEntity<ApiResponse<WorkflowResponse>> approveAndExecuteWorkflow(@PathVariable String id) {
        WorkflowResponse placeholder = new WorkflowResponse(
                id,
                "Approved Prompt Placeholder",
                WorkflowStatus.APPROVED,
                150.0,
                LocalDateTime.now(),
                null,
                new ArrayList<>()
        );
        return ResponseEntity.ok(ApiResponse.success("Workflow approved for execution", placeholder));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel workflow and initiate Algorand Escrow refund")
    public ResponseEntity<ApiResponse<WorkflowResponse>> cancelWorkflow(@PathVariable String id) {
        WorkflowResponse placeholder = new WorkflowResponse(
                id,
                "Cancelled Prompt Placeholder",
                WorkflowStatus.CANCELLED,
                0.0,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new ArrayList<>()
        );
        return ResponseEntity.ok(ApiResponse.success("Workflow cancelled successfully", placeholder));
    }
}
