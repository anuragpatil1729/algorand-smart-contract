package com.agentmesh.router.execution.dto;

import java.util.ArrayList;
import java.util.List;

public class WorkflowExecutionStatusDto {

    private String workflowId;
    private String status;
    private Integer currentStage = 1;
    private Double progressPercentage = 0.0;
    private Integer totalTasksCount = 0;
    private Integer completedTasksCount = 0;
    private Integer failedTasksCount = 0;
    private List<String> runningTasks = new ArrayList<>();
    private List<String> completedTasks = new ArrayList<>();
    private List<String> failedTasks = new ArrayList<>();
    private Long startTime;
    private Long completedTime;

    public WorkflowExecutionStatusDto() {}

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getCurrentStage() { return currentStage; }
    public void setCurrentStage(Integer currentStage) { this.currentStage = currentStage; }

    public Double getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Double progressPercentage) { this.progressPercentage = progressPercentage; }

    public Integer getTotalTasksCount() { return totalTasksCount; }
    public void setTotalTasksCount(Integer totalTasksCount) { this.totalTasksCount = totalTasksCount; }

    public Integer getCompletedTasksCount() { return completedTasksCount; }
    public void setCompletedTasksCount(Integer completedTasksCount) { this.completedTasksCount = completedTasksCount; }

    public Integer getFailedTasksCount() { return failedTasksCount; }
    public void setFailedTasksCount(Integer failedTasksCount) { this.failedTasksCount = failedTasksCount; }

    public List<String> getRunningTasks() { return runningTasks; }
    public void setRunningTasks(List<String> runningTasks) { this.runningTasks = runningTasks; }

    public List<String> getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(List<String> completedTasks) { this.completedTasks = completedTasks; }

    public List<String> getFailedTasks() { return failedTasks; }
    public void setFailedTasks(List<String> failedTasks) { this.failedTasks = failedTasks; }

    public Long getStartTime() { return startTime; }
    public void setStartTime(Long startTime) { this.startTime = startTime; }

    public Long getCompletedTime() { return completedTime; }
    public void setCompletedTime(Long completedTime) { this.completedTime = completedTime; }
}
