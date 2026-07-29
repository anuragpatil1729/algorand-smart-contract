package com.agentmesh.router.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsSummaryDto {
    private Long totalAgents;
    private Long activeWorkflows;
    private Long completedWorkflows;
    private Long totalTransactions;
    private Double overallSuccessRate;
    private Double avgExecutionTimeSeconds;
    private Double totalRevenueAlgos;
    private Map<String, Long> agentUsageMap;
    private Map<String, Double> costDistributionMap;
    private List<DailyMetricDto> recentActivity;

    public AnalyticsSummaryDto() {}

    public AnalyticsSummaryDto(Long totalAgents, Long activeWorkflows, Long completedWorkflows, Long totalTransactions, Double overallSuccessRate, Double avgExecutionTimeSeconds, Double totalRevenueAlgos, Map<String, Long> agentUsageMap, Map<String, Double> costDistributionMap, List<DailyMetricDto> recentActivity) {
        this.totalAgents = totalAgents;
        this.activeWorkflows = activeWorkflows;
        this.completedWorkflows = completedWorkflows;
        this.totalTransactions = totalTransactions;
        this.overallSuccessRate = overallSuccessRate;
        this.avgExecutionTimeSeconds = avgExecutionTimeSeconds;
        this.totalRevenueAlgos = totalRevenueAlgos;
        this.agentUsageMap = agentUsageMap;
        this.costDistributionMap = costDistributionMap;
        this.recentActivity = recentActivity;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long totalAgents;
        private Long activeWorkflows;
        private Long completedWorkflows;
        private Long totalTransactions;
        private Double overallSuccessRate;
        private Double avgExecutionTimeSeconds;
        private Double totalRevenueAlgos;
        private Map<String, Long> agentUsageMap;
        private Map<String, Double> costDistributionMap;
        private List<DailyMetricDto> recentActivity;

        public Builder totalAgents(Long totalAgents) { this.totalAgents = totalAgents; return this; }
        public Builder activeWorkflows(Long activeWorkflows) { this.activeWorkflows = activeWorkflows; return this; }
        public Builder completedWorkflows(Long completedWorkflows) { this.completedWorkflows = completedWorkflows; return this; }
        public Builder totalTransactions(Long totalTransactions) { this.totalTransactions = totalTransactions; return this; }
        public Builder overallSuccessRate(Double overallSuccessRate) { this.overallSuccessRate = overallSuccessRate; return this; }
        public Builder avgExecutionTimeSeconds(Double avgExecutionTimeSeconds) { this.avgExecutionTimeSeconds = avgExecutionTimeSeconds; return this; }
        public Builder totalRevenueAlgos(Double totalRevenueAlgos) { this.totalRevenueAlgos = totalRevenueAlgos; return this; }
        public Builder agentUsageMap(Map<String, Long> agentUsageMap) { this.agentUsageMap = agentUsageMap; return this; }
        public Builder costDistributionMap(Map<String, Double> costDistributionMap) { this.costDistributionMap = costDistributionMap; return this; }
        public Builder recentActivity(List<DailyMetricDto> recentActivity) { this.recentActivity = recentActivity; return this; }

        public AnalyticsSummaryDto build() {
            return new AnalyticsSummaryDto(totalAgents, activeWorkflows, completedWorkflows, totalTransactions, overallSuccessRate, avgExecutionTimeSeconds, totalRevenueAlgos, agentUsageMap, costDistributionMap, recentActivity);
        }
    }

    public static class DailyMetricDto {
        private String date;
        private Integer workflows;
        private Double revenue;

        public DailyMetricDto() {}
        public DailyMetricDto(String date, Integer workflows, Double revenue) {
            this.date = date;
            this.workflows = workflows;
            this.revenue = revenue;
        }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public Integer getWorkflows() { return workflows; }
        public void setWorkflows(Integer workflows) { this.workflows = workflows; }
        public Double getRevenue() { return revenue; }
        public void setRevenue(Double revenue) { this.revenue = revenue; }
    }

    public Long getTotalAgents() { return totalAgents; }
    public void setTotalAgents(Long totalAgents) { this.totalAgents = totalAgents; }
    public Long getActiveWorkflows() { return activeWorkflows; }
    public void setActiveWorkflows(Long activeWorkflows) { this.activeWorkflows = activeWorkflows; }
    public Long getCompletedWorkflows() { return completedWorkflows; }
    public void setCompletedWorkflows(Long completedWorkflows) { this.completedWorkflows = completedWorkflows; }
    public Long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(Long totalTransactions) { this.totalTransactions = totalTransactions; }
    public Double getOverallSuccessRate() { return overallSuccessRate; }
    public void setOverallSuccessRate(Double overallSuccessRate) { this.overallSuccessRate = overallSuccessRate; }
    public Double getAvgExecutionTimeSeconds() { return avgExecutionTimeSeconds; }
    public void setAvgExecutionTimeSeconds(Double avgExecutionTimeSeconds) { this.avgExecutionTimeSeconds = avgExecutionTimeSeconds; }
    public Double getTotalRevenueAlgos() { return totalRevenueAlgos; }
    public void setTotalRevenueAlgos(Double totalRevenueAlgos) { this.totalRevenueAlgos = totalRevenueAlgos; }
    public Map<String, Long> getAgentUsageMap() { return agentUsageMap; }
    public void setAgentUsageMap(Map<String, Long> agentUsageMap) { this.agentUsageMap = agentUsageMap; }
    public Map<String, Double> getCostDistributionMap() { return costDistributionMap; }
    public void setCostDistributionMap(Map<String, Double> costDistributionMap) { this.costDistributionMap = costDistributionMap; }
    public List<DailyMetricDto> getRecentActivity() { return recentActivity; }
    public void setRecentActivity(List<DailyMetricDto> recentActivity) { this.recentActivity = recentActivity; }
}
