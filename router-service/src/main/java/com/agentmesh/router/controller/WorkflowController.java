package com.agentmesh.router.controller;

import com.agentmesh.router.dto.WorkflowRequest;
import com.agentmesh.router.dto.WorkflowResponse;
import com.agentmesh.router.service.AgentMeshService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final AgentMeshService agentMeshService;

    public WorkflowController(AgentMeshService agentMeshService) {
        this.agentMeshService = agentMeshService;
    }

    @PostMapping
    public ResponseEntity<WorkflowResponse> createWorkflow(@RequestBody WorkflowRequest request) {
        WorkflowResponse response = agentMeshService.createWorkflow(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowResponse> getWorkflow(@PathVariable String id) {
        WorkflowResponse response = agentMeshService.getWorkflowDetails(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<WorkflowResponse>> listWorkflows() {
        List<WorkflowResponse> workflows = agentMeshService.listAllWorkflows();
        return ResponseEntity.ok(workflows);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<WorkflowResponse> approveWorkflow(@PathVariable String id) {
        WorkflowResponse response = agentMeshService.approveWorkflow(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/execute")
    public ResponseEntity<WorkflowResponse> executeWorkflow(@PathVariable String id) {
        WorkflowResponse response = agentMeshService.executeWorkflow(id);
        return ResponseEntity.ok(response);
    }
}
