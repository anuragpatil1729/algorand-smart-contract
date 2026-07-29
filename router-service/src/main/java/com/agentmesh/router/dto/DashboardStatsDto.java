package com.agentmesh.router.dto;

import java.util.Map;

public class DashboardStatsDto {

    private long totalAgents;
    private long onlineAgents;
    private long busyAgents;
    private long degradedAgents;
    private long offlineAgents;
    private double averageHealthScore;
    private double averageResponseTime;
    private double averageBasePrice;
    private Map<String, Integer> capabilityCounts;
    private Map<String, Double> agentLoads;

    public DashboardStatsDto() {}

    public long getTotalAgents() { return totalAgents; }
    public void setTotalAgents(long totalAgents) { this.totalAgents = totalAgents; }
    public long getOnlineAgents() { return onlineAgents; }
    public void setOnlineAgents(long onlineAgents) { this.onlineAgents = onlineAgents; }
    public long getBusyAgents() { return busyAgents; }
    public void setBusyAgents(long busyAgents) { this.busyAgents = busyAgents; }
    public long getDegradedAgents() { return degradedAgents; }
    public void setDegradedAgents(long degradedAgents) { this.degradedAgents = degradedAgents; }
    public long getOfflineAgents() { return offlineAgents; }
    public void setOfflineAgents(long offlineAgents) { this.offlineAgents = offlineAgents; }
    public double getAverageHealthScore() { return averageHealthScore; }
    public void setAverageHealthScore(double averageHealthScore) { this.averageHealthScore = averageHealthScore; }
    public double getAverageResponseTime() { return averageResponseTime; }
    public void setAverageResponseTime(double averageResponseTime) { this.averageResponseTime = averageResponseTime; }
    public double getAverageBasePrice() { return averageBasePrice; }
    public void setAverageBasePrice(double averageBasePrice) { this.averageBasePrice = averageBasePrice; }
    public Map<String, Integer> getCapabilityCounts() { return capabilityCounts; }
    public void setCapabilityCounts(Map<String, Integer> capabilityCounts) { this.capabilityCounts = capabilityCounts; }
    public Map<String, Double> getAgentLoads() { return agentLoads; }
    public void setAgentLoads(Map<String, Double> agentLoads) { this.agentLoads = agentLoads; }
}
