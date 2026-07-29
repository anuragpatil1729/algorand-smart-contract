package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.WorkflowPlanRequestDto;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;

import java.util.List;

public interface WorkflowPlanner {

    WorkflowPlanResponseDto createPlan(WorkflowPlanRequestDto request);

    WorkflowPlanResponseDto getPlan(String workflowId);

    List<String> validatePlan(WorkflowPlanResponseDto plan);

    WorkflowPlanResponseDto optimizePlan(WorkflowPlanResponseDto plan);
}
