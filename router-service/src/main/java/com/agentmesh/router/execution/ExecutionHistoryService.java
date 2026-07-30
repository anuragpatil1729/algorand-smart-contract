package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionEvent;
import com.agentmesh.router.execution.dto.WorkflowExecutionStatusDto;
import com.agentmesh.router.execution.dto.WorkflowResult;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExecutionHistoryService {

    private final Map<String, ExecutionContext> activeContexts = new ConcurrentHashMap<>();
    private final Map<String, WorkflowResult> completedResults = new ConcurrentHashMap<>();

    public void storeContext(ExecutionContext context) {
        if (context != null && context.getWorkflowId() != null) {
            activeContexts.put(context.getWorkflowId(), context);
        }
    }

    public ExecutionContext getContext(String workflowId) {
        return activeContexts.get(workflowId);
    }

    public void storeResult(String workflowId, WorkflowResult result) {
        if (workflowId != null && result != null) {
            completedResults.put(workflowId, result);
        }
    }

    public WorkflowResult getResult(String workflowId) {
        return completedResults.get(workflowId);
    }

    public WorkflowExecutionStatusDto getStatusDto(String workflowId) {
        ExecutionContext context = activeContexts.get(workflowId);
        if (context == null) return null;

        WorkflowExecutionStatusDto dto = new WorkflowExecutionStatusDto();
        dto.setWorkflowId(workflowId);
        dto.setStatus(context.getWorkflowState() != null ? context.getWorkflowState().name() : "UNKNOWN");
        dto.setCurrentStage(context.getCurrentStage());
        dto.setStartTime(context.getStartTime());
        dto.setCompletedTime(context.getCompletedTime());

        int total = context.getAssignmentPlan() != null && context.getAssignmentPlan().getAssignments() != null ?
                context.getAssignmentPlan().getAssignments().size() : 0;
        int completed = context.getCompletedTasks().size();
        int failed = context.getFailedTasks().size();

        dto.setTotalTasksCount(total);
        dto.setCompletedTasksCount(completed);
        dto.setFailedTasksCount(failed);
        dto.setProgressPercentage(total > 0 ? Math.round((completed * 100.0 / total) * 10.0) / 10.0 : 0.0);

        dto.setRunningTasks(context.getRunningTasks());
        dto.setCompletedTasks(context.getCompletedTasks());
        dto.setFailedTasks(context.getFailedTasks());

        return dto;
    }

    public List<String> getLogs(String workflowId) {
        ExecutionContext context = activeContexts.get(workflowId);
        return context != null ? context.getExecutionLogs() : Collections.emptyList();
    }

    public List<ExecutionEvent> getEvents(String workflowId) {
        ExecutionContext context = activeContexts.get(workflowId);
        return context != null ? context.getEventHistory() : Collections.emptyList();
    }
}
