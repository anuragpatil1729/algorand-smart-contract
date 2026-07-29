package com.agentmesh.router.service;

import com.agentmesh.router.model.Task;
import com.agentmesh.router.model.Workflow;

import java.util.List;

public interface PlannerService {
    List<Task> decomposePromptIntoTasks(Workflow workflow);
}
