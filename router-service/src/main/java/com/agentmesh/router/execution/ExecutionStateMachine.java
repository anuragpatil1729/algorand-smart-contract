package com.agentmesh.router.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ExecutionStateMachine {

    private static final Logger log = LoggerFactory.getLogger(ExecutionStateMachine.class);

    public enum WorkflowState {
        CREATED,
        QUEUED,
        PLANNING_COMPLETE,
        ASSIGNMENT_COMPLETE,
        EXECUTION_STARTED,
        RUNNING,
        PAUSED,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    public enum TaskState {
        PENDING,
        READY,
        RUNNING,
        COMPLETED,
        FAILED,
        RETRYING,
        FALLBACK,
        FAILED_PERMANENTLY
    }

    public boolean isValidWorkflowTransition(WorkflowState current, WorkflowState target) {
        if (current == target) return true;
        switch (current) {
            case CREATED:
                return target == WorkflowState.QUEUED || target == WorkflowState.CANCELLED;
            case QUEUED:
                return target == WorkflowState.PLANNING_COMPLETE || target == WorkflowState.ASSIGNMENT_COMPLETE || target == WorkflowState.EXECUTION_STARTED || target == WorkflowState.CANCELLED;
            case PLANNING_COMPLETE:
                return target == WorkflowState.ASSIGNMENT_COMPLETE || target == WorkflowState.CANCELLED;
            case ASSIGNMENT_COMPLETE:
                return target == WorkflowState.EXECUTION_STARTED || target == WorkflowState.CANCELLED;
            case EXECUTION_STARTED:
                return target == WorkflowState.RUNNING || target == WorkflowState.FAILED || target == WorkflowState.CANCELLED;
            case RUNNING:
                return target == WorkflowState.COMPLETED || target == WorkflowState.FAILED || target == WorkflowState.CANCELLED || target == WorkflowState.PAUSED;
            case PAUSED:
                return target == WorkflowState.RUNNING || target == WorkflowState.CANCELLED;
            case COMPLETED:
            case FAILED:
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }

    public boolean isValidTaskTransition(TaskState current, TaskState target) {
        if (current == target) return true;
        switch (current) {
            case PENDING:
                return target == TaskState.READY || target == TaskState.FAILED_PERMANENTLY;
            case READY:
                return target == TaskState.RUNNING || target == TaskState.FAILED_PERMANENTLY;
            case RUNNING:
                return target == TaskState.COMPLETED || target == TaskState.FAILED || target == TaskState.RETRYING;
            case FAILED:
                return target == TaskState.RETRYING || target == TaskState.FALLBACK || target == TaskState.FAILED_PERMANENTLY;
            case RETRYING:
                return target == TaskState.RUNNING || target == TaskState.COMPLETED || target == TaskState.FAILED || target == TaskState.FALLBACK;
            case FALLBACK:
                return target == TaskState.READY || target == TaskState.RUNNING || target == TaskState.COMPLETED || target == TaskState.FAILED_PERMANENTLY;
            case COMPLETED:
            case FAILED_PERMANENTLY:
                return false;
            default:
                return false;
        }
    }

    public WorkflowState transitionWorkflow(WorkflowState current, WorkflowState target) {
        if (!isValidWorkflowTransition(current, target)) {
            log.warn("Invalid Workflow state transition from {} to {}", current, target);
            throw new IllegalStateException("Cannot transition workflow from " + current + " to " + target);
        }
        log.info("Workflow transitioned from {} to {}", current, target);
        return target;
    }

    public TaskState transitionTask(String taskId, TaskState current, TaskState target) {
        if (!isValidTaskTransition(current, target)) {
            log.warn("Invalid Task state transition for task '{}' from {} to {}", taskId, current, target);
            throw new IllegalStateException("Cannot transition task '" + taskId + "' from " + current + " to " + target);
        }
        log.info("Task '{}' transitioned from {} to {}", taskId, current, target);
        return target;
    }
}
