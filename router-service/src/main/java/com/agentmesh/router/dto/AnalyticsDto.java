package com.agentmesh.router.dto;

import java.util.List;

public class AnalyticsDto {
    private long totalWorkflows;
    private long completedWorkflows;
    private long activeAgents;
    private double totalRevenue;
    private double successRate;
    private List<TaskTypeMetric> taskTypeDistribution;

    public AnalyticsDto() {}

    public AnalyticsDto(long totalWorkflows, long completedWorkflows, long activeAgents, double totalRevenue, double successRate, List<TaskTypeMetric> taskTypeDistribution) {
        this.totalWorkflows = totalWorkflows;
        this.completedWorkflows = completedWorkflows;
        this.activeAgents = activeAgents;
        this.totalRevenue = totalRevenue;
        this.successRate = successRate;
        this.taskTypeDistribution = taskTypeDistribution;
    }

    public static class TaskTypeMetric {
        private String taskType;
        private int count;

        public TaskTypeMetric() {}
        public TaskTypeMetric(String taskType, int count) {
            this.taskType = taskType;
            this.count = count;
        }

        public String getTaskType() { return taskType; }
        public void setTaskType(String taskType) { this.taskType = taskType; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }

    public long getTotalWorkflows() { return totalWorkflows; }
    public void setTotalWorkflows(long totalWorkflows) { this.totalWorkflows = totalWorkflows; }
    public long getCompletedWorkflows() { return completedWorkflows; }
    public void setCompletedWorkflows(long completedWorkflows) { this.completedWorkflows = completedWorkflows; }
    public long getActiveAgents() { return activeAgents; }
    public void setActiveAgents(long activeAgents) { this.activeAgents = activeAgents; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public double getSuccessRate() { return successRate; }
    public void setSuccessRate(double successRate) { this.successRate = successRate; }
    public List<TaskTypeMetric> getTaskTypeDistribution() { return taskTypeDistribution; }
    public void setTaskTypeDistribution(List<TaskTypeMetric> taskTypeDistribution) { this.taskTypeDistribution = taskTypeDistribution; }
}
