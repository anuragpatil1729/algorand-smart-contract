package com.agentmesh.router.dto.mapper;

import com.agentmesh.router.dto.WorkflowRequest;
import com.agentmesh.router.dto.WorkflowResponse;
import com.agentmesh.router.model.Task;
import com.agentmesh.router.model.Workflow;
import com.agentmesh.router.model.enums.WorkflowStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WorkflowMapper {

    public static Workflow toEntity(WorkflowRequest request) {
        if (request == null) return null;
        return Workflow.builder()
                .id("wf-" + UUID.randomUUID().toString().substring(0, 8))
                .prompt(request.getPrompt())
                .status(WorkflowStatus.PENDING_APPROVAL)
                .totalCost(0.0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static WorkflowResponse toDto(Workflow workflow) {
        if (workflow == null) return null;
        List<WorkflowResponse.TaskResponse> taskDtos = new ArrayList<>();
        if (workflow.getTasks() != null) {
            taskDtos = workflow.getTasks().stream().map(WorkflowMapper::toTaskDto).collect(Collectors.toList());
        }

        WorkflowResponse response = new WorkflowResponse(
                workflow.getId(),
                workflow.getPrompt(),
                workflow.getStatusEnum(),
                workflow.getTotalCost(),
                workflow.getCreatedAt(),
                workflow.getCompletedAt(),
                taskDtos
        );
        response.setEscrowAddress(workflow.getEscrowAddress());
        response.setEscrowStatus(workflow.getEscrowStatus());
        response.setAggregatedResult(workflow.getAggregatedResult());
        return response;
    }

    public static WorkflowResponse.TaskResponse toTaskDto(Task task) {
        if (task == null) return null;
        return new WorkflowResponse.TaskResponse(
                task.getId(),
                task.getTaskType(),
                task.getDescription(),
                task.getAssignedAgent() != null ? task.getAssignedAgent().getId() : null,
                task.getStatus(),
                task.getPrice(),
                task.getDependency(),
                task.getExecutionTime(),
                task.getOutput()
        );
    }
}
