package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionEvent;
import com.agentmesh.router.execution.dto.ExecutionTaskResponse;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskAssignment;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExecutionContext {

    private final String workflowId;
    private final AssignmentPlan assignmentPlan;
    private ExecutionStateMachine.WorkflowState workflowState = ExecutionStateMachine.WorkflowState.CREATED;
    private int currentStage = 1;
    private final long startTime = System.currentTimeMillis();
    private Long completedTime;

    private final Map<String, ExecutionStateMachine.TaskState> taskStates = new ConcurrentHashMap<>();
    private final Map<String, Object> taskOutputs = new ConcurrentHashMap<>();
    private final Map<String, ExecutionTaskResponse> taskResponses = new ConcurrentHashMap<>();
    private final Map<String, TaskAssignment> currentAssignments = new ConcurrentHashMap<>();
    private final List<ExecutionEvent> eventHistory = new CopyOnWriteArrayList<>();
    private final List<String> executionLogs = new CopyOnWriteArrayList<>();

    public ExecutionContext(String workflowId, AssignmentPlan assignmentPlan) {
        this.workflowId = workflowId;
        this.assignmentPlan = assignmentPlan;

        if (assignmentPlan != null && assignmentPlan.getAssignments() != null) {
            for (TaskAssignment assignment : assignmentPlan.getAssignments()) {
                this.currentAssignments.put(assignment.getTaskId(), assignment);
                this.taskStates.put(assignment.getTaskId(), ExecutionStateMachine.TaskState.PENDING);
            }
        }
    }

    public String getWorkflowId() { return workflowId; }
    public AssignmentPlan getAssignmentPlan() { return assignmentPlan; }

    public synchronized ExecutionStateMachine.WorkflowState getWorkflowState() { return workflowState; }
    public synchronized void setWorkflowState(ExecutionStateMachine.WorkflowState workflowState) { this.workflowState = workflowState; }

    public synchronized int getCurrentStage() { return currentStage; }
    public synchronized void setCurrentStage(int currentStage) { this.currentStage = currentStage; }

    public long getStartTime() { return startTime; }
    public synchronized Long getCompletedTime() { return completedTime; }
    public synchronized void setCompletedTime(Long completedTime) { this.completedTime = completedTime; }

    public Map<String, ExecutionStateMachine.TaskState> getTaskStates() { return taskStates; }
    public Map<String, Object> getTaskOutputs() { return taskOutputs; }
    public Map<String, ExecutionTaskResponse> getTaskResponses() { return taskResponses; }
    public Map<String, TaskAssignment> getCurrentAssignments() { return currentAssignments; }
    public List<ExecutionEvent> getEventHistory() { return eventHistory; }
    public List<String> getExecutionLogs() { return executionLogs; }

    public void updateTaskState(String taskId, ExecutionStateMachine.TaskState state) {
        taskStates.put(taskId, state);
    }

    public void setTaskOutput(String taskId, Object output) {
        if (output != null) taskOutputs.put(taskId, output);
    }

    public void setTaskResponse(String taskId, ExecutionTaskResponse response) {
        if (response != null) taskResponses.put(taskId, response);
    }

    public void updateTaskAssignment(String taskId, TaskAssignment assignment) {
        if (assignment != null) currentAssignments.put(taskId, assignment);
    }

    public void addEvent(ExecutionEvent event) {
        if (event != null) eventHistory.add(event);
    }

    public void addLog(String log) {
        if (log != null) executionLogs.add(log);
    }

    public List<String> getRunningTasks() {
        List<String> list = new ArrayList<>();
        taskStates.forEach((taskId, state) -> {
            if (state == ExecutionStateMachine.TaskState.RUNNING || state == ExecutionStateMachine.TaskState.RETRYING) {
                list.add(taskId);
            }
        });
        return list;
    }

    public List<String> getCompletedTasks() {
        List<String> list = new ArrayList<>();
        taskStates.forEach((taskId, state) -> {
            if (state == ExecutionStateMachine.TaskState.COMPLETED) {
                list.add(taskId);
            }
        });
        return list;
    }

    public List<String> getFailedTasks() {
        List<String> list = new ArrayList<>();
        taskStates.forEach((taskId, state) -> {
            if (state == ExecutionStateMachine.TaskState.FAILED_PERMANENTLY) {
                list.add(taskId);
            }
        });
        return list;
    }
}
