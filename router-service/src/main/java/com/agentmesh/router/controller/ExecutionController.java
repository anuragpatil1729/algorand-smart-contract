package com.agentmesh.router.controller;

import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.execution.ExecutionHistoryService;
import com.agentmesh.router.execution.ExecutionMonitor;
import com.agentmesh.router.execution.WorkflowExecutor;
import com.agentmesh.router.execution.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/execution")
@Tag(name = "Execution Controller", description = "Endpoints for workflow execution orchestration, monitoring, control, and event telemetry")
public class ExecutionController {

    private final WorkflowExecutor workflowExecutor;
    private final ExecutionHistoryService historyService;
    private final ExecutionMonitor executionMonitor;

    public ExecutionController(
            WorkflowExecutor workflowExecutor,
            ExecutionHistoryService historyService,
            ExecutionMonitor executionMonitor
    ) {
        this.workflowExecutor = workflowExecutor;
        this.historyService = historyService;
        this.executionMonitor = executionMonitor;
    }

    @PostMapping("/start")
    @Operation(summary = "Start workflow execution for an AssignmentPlan")
    public ResponseEntity<ApiResponse<WorkflowResult>> startExecution(@RequestBody WorkflowExecutionRequest request) {
        if (request == null || request.getAssignmentPlan() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("AssignmentPlan must be provided in request body"));
        }

        WorkflowResult result = workflowExecutor.executeWorkflowSync(request);
        return ResponseEntity.ok(ApiResponse.success("Workflow execution finished", result));
    }

    @GetMapping("/{workflowId}")
    @Operation(summary = "Retrieve current workflow execution status and progress")
    public ResponseEntity<ApiResponse<WorkflowExecutionStatusDto>> getStatus(@PathVariable String workflowId) {
        WorkflowExecutionStatusDto status = historyService.getStatusDto(workflowId);
        if (status == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("Execution context not found for workflowId: " + workflowId));
        }
        return ResponseEntity.ok(ApiResponse.success("Workflow status retrieved", status));
    }

    @PostMapping("/{workflowId}/cancel")
    @Operation(summary = "Cancel an active workflow execution")
    public ResponseEntity<ApiResponse<String>> cancelExecution(@PathVariable String workflowId) {
        boolean cancelled = workflowExecutor.cancelWorkflow(workflowId);
        if (cancelled) {
            return ResponseEntity.ok(ApiResponse.success("Workflow cancelled successfully", workflowId));
        }
        return ResponseEntity.status(400).body(ApiResponse.error("Unable to cancel workflow. Workflow not found or already terminal."));
    }

    @PostMapping("/{workflowId}/pause")
    @Operation(summary = "Pause a running workflow execution")
    public ResponseEntity<ApiResponse<String>> pauseExecution(@PathVariable String workflowId) {
        boolean paused = workflowExecutor.pauseWorkflow(workflowId);
        if (paused) {
            return ResponseEntity.ok(ApiResponse.success("Workflow paused successfully", workflowId));
        }
        return ResponseEntity.status(400).body(ApiResponse.error("Unable to pause workflow. Workflow not in RUNNING state."));
    }

    @PostMapping("/{workflowId}/resume")
    @Operation(summary = "Resume a paused workflow execution")
    public ResponseEntity<ApiResponse<String>> resumeExecution(@PathVariable String workflowId) {
        boolean resumed = workflowExecutor.resumeWorkflow(workflowId);
        if (resumed) {
            return ResponseEntity.ok(ApiResponse.success("Workflow resumed successfully", workflowId));
        }
        return ResponseEntity.status(400).body(ApiResponse.error("Unable to resume workflow. Workflow not in PAUSED state."));
    }

    @GetMapping("/{workflowId}/logs")
    @Operation(summary = "Retrieve execution logs for a workflow ID")
    public ResponseEntity<ApiResponse<List<String>>> getLogs(@PathVariable String workflowId) {
        List<String> logs = historyService.getLogs(workflowId);
        return ResponseEntity.ok(ApiResponse.success("Execution logs retrieved", logs));
    }

    @GetMapping("/{workflowId}/events")
    @Operation(summary = "Retrieve event trajectory for a workflow ID")
    public ResponseEntity<ApiResponse<List<ExecutionEvent>>> getEvents(@PathVariable String workflowId) {
        List<ExecutionEvent> events = historyService.getEvents(workflowId);
        return ResponseEntity.ok(ApiResponse.success("Execution events retrieved", events));
    }

    @GetMapping("/metrics")
    @Operation(summary = "Retrieve real-time execution telemetry and performance metrics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMetrics() {
        Map<String, Object> metrics = executionMonitor.getMetrics();
        return ResponseEntity.ok(ApiResponse.success("Execution metrics retrieved", metrics));
    }
}
