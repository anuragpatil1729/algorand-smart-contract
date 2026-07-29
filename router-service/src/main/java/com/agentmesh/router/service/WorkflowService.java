package com.agentmesh.router.service;

import com.agentmesh.router.dto.WorkflowRequest;
import com.agentmesh.router.dto.WorkflowResponse;
import com.agentmesh.router.model.Workflow;

import java.util.List;

public interface WorkflowService {
    WorkflowResponse createWorkflow(WorkflowRequest request);
    WorkflowResponse getWorkflowById(String id);
    List<WorkflowResponse> getAllWorkflows();
    WorkflowResponse approveAndExecuteWorkflow(String id);
    WorkflowResponse cancelWorkflow(String id);
}
