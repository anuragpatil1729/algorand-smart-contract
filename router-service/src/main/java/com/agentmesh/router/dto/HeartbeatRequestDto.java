package com.agentmesh.router.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HeartbeatRequestDto {

    @JsonProperty("agentId")
    private String agentId;

    @JsonProperty("cpuUsagePercent")
    private Double cpuUsagePercent = 0.0;

    @JsonProperty("memoryUsageMb")
    private Double memoryUsageMb = 0.0;

    @JsonProperty("runningTasks")
    private Integer runningTasks = 0;

    @JsonProperty("queueSize")
    private Integer queueSize = 0;

    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("lastExecutionTime")
    private Double lastExecutionTime = 0.0;

    @JsonProperty("status")
    private String status;

    public HeartbeatRequestDto() {}

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public Double getCpuUsagePercent() { return cpuUsagePercent; }
    public void setCpuUsagePercent(Double cpuUsagePercent) { this.cpuUsagePercent = cpuUsagePercent; }
    public Double getMemoryUsageMb() { return memoryUsageMb; }
    public void setMemoryUsageMb(Double memoryUsageMb) { this.memoryUsageMb = memoryUsageMb; }
    public Integer getRunningTasks() { return runningTasks; }
    public void setRunningTasks(Integer runningTasks) { this.runningTasks = runningTasks; }
    public Integer getQueueSize() { return queueSize; }
    public void setQueueSize(Integer queueSize) { this.queueSize = queueSize; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    public Double getLastExecutionTime() { return lastExecutionTime; }
    public void setLastExecutionTime(Double lastExecutionTime) { this.lastExecutionTime = lastExecutionTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
