package com.agentmesh.router.dto;

import com.agentmesh.router.model.Agent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AgentRegistryDto {

    private String id;
    private String name;
    private String description;
    private String endpoint;
    private String walletAddress;
    private String version;
    private Double rating;
    private Double successRate;
    private Double basePrice;
    private String status;
    private Double healthScore;
    private Double averageResponseTime;
    private Integer runningTasks;
    private Integer maxConcurrency;
    private Double currentLoad;
    private Double cpuUsage;
    private Double memoryUsage;
    private Integer queueSize;
    private LocalDateTime lastHeartbeat;
    private Long completedTasks;
    private Long failedTasks;
    private Long totalRequests;
    private Double totalEarnings;
    private LocalDateTime registrationTime;
    private List<String> capabilities;

    public AgentRegistryDto() {}

    public static AgentRegistryDto fromEntity(Agent agent) {
        if (agent == null) return null;
        AgentRegistryDto dto = new AgentRegistryDto();
        dto.setId(agent.getId());
        dto.setName(agent.getName());
        dto.setDescription(agent.getDescription());
        dto.setEndpoint(agent.getEndpoint());
        dto.setWalletAddress(agent.getWalletAddress());
        dto.setVersion(agent.getVersion());
        dto.setRating(agent.getRating());
        dto.setSuccessRate(agent.getSuccessRate());
        dto.setBasePrice(agent.getBasePrice());
        dto.setStatus(agent.getHealthStatus());
        dto.setHealthScore(agent.getHealthScore());
        dto.setAverageResponseTime(agent.getAverageResponseTime());
        dto.setRunningTasks(agent.getRunningTasks());
        dto.setMaxConcurrency(agent.getMaxConcurrency());
        dto.setCurrentLoad(agent.getCurrentLoad());
        dto.setCpuUsage(agent.getCpuUsage());
        dto.setMemoryUsage(agent.getMemoryUsage());
        dto.setQueueSize(agent.getQueueSize());
        dto.setLastHeartbeat(agent.getLastHeartbeat());
        dto.setCompletedTasks(agent.getCompletedTasks());
        dto.setFailedTasks(agent.getFailedTasks());
        dto.setTotalRequests(agent.getTotalRequests());
        dto.setTotalEarnings(agent.getTotalEarnings());
        dto.setRegistrationTime(agent.getRegistrationTime());
        if (agent.getCapabilities() != null && !agent.getCapabilities().isBlank()) {
            dto.setCapabilities(Arrays.asList(agent.getCapabilities().split(",")));
        } else {
            dto.setCapabilities(Collections.emptyList());
        }
        return dto;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getWalletAddress() { return walletAddress; }
    public void setWalletAddress(String walletAddress) { this.walletAddress = walletAddress; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getHealthScore() { return healthScore; }
    public void setHealthScore(Double healthScore) { this.healthScore = healthScore; }
    public Double getAverageResponseTime() { return averageResponseTime; }
    public void setAverageResponseTime(Double averageResponseTime) { this.averageResponseTime = averageResponseTime; }
    public Integer getRunningTasks() { return runningTasks; }
    public void setRunningTasks(Integer runningTasks) { this.runningTasks = runningTasks; }
    public Integer getMaxConcurrency() { return maxConcurrency; }
    public void setMaxConcurrency(Integer maxConcurrency) { this.maxConcurrency = maxConcurrency; }
    public Double getCurrentLoad() { return currentLoad; }
    public void setCurrentLoad(Double currentLoad) { this.currentLoad = currentLoad; }
    public Double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(Double cpuUsage) { this.cpuUsage = cpuUsage; }
    public Double getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(Double memoryUsage) { this.memoryUsage = memoryUsage; }
    public Integer getQueueSize() { return queueSize; }
    public void setQueueSize(Integer queueSize) { this.queueSize = queueSize; }
    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    public Long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(Long completedTasks) { this.completedTasks = completedTasks; }
    public Long getFailedTasks() { return failedTasks; }
    public void setFailedTasks(Long failedTasks) { this.failedTasks = failedTasks; }
    public Long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(Long totalRequests) { this.totalRequests = totalRequests; }
    public Double getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(Double totalEarnings) { this.totalEarnings = totalEarnings; }
    public LocalDateTime getRegistrationTime() { return registrationTime; }
    public void setRegistrationTime(LocalDateTime registrationTime) { this.registrationTime = registrationTime; }
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
}
