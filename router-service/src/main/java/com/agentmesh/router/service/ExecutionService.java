package com.agentmesh.router.service;

import com.agentmesh.router.model.Task;
import com.agentmesh.router.model.Workflow;

public interface ExecutionService {
    void executeWorkflow(Workflow workflow);
    boolean executeTask(Task task);
}
