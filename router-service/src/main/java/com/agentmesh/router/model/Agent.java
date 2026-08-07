package com.agentmesh.router.model;

import com.agentmesh.router.model.enums.HealthStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agents")
public class Agent {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String endpoint;

    @Column(name = "wallet_address")
    private String walletAddress;

    private String version = "1.0.0";

    private Double rating = 4.5;

    @Column(name = "success_rate")
    private Double successRate = 95.0;

    @Column(name = "base_price")
    private Double basePrice = 50.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status", nullable = false)
    private HealthStatus healthStatus = HealthStatus.ONLINE;

    @Column(name = "health_score")
    private Double healthScore = 100.0;

    @Column(name = "average_response_time")
    private Double averageResponseTime = 50.0;

    @Column(name = "running_tasks")
    private Integer runningTasks = 0;

    @Column(name = "max_concurrency")
    private Integer maxConcurrency = 5;

    @Column(name = "current_load")
    private Double currentLoad = 0.0;

    @Column(name = "cpu_usage")
    private Double cpuUsage = 0.0;

    @Column(name = "memory_usage")
    private Double memoryUsage = 0.0;

    @Column(name = "queue_size")
    private Integer queueSize = 0;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @Column(name = "completed_tasks")
    private Long completedTasks = 0L;

    @Column(name = "failed_tasks")
    private Long failedTasks = 0L;

    @Column(name = "total_requests")
    private Long totalRequests = 0L;

    @Column(name = "total_earnings")
    private Double totalEarnings = 0.0;

    @Column(name = "peak_concurrency")
    private Integer peakConcurrency = 0;

    @Column(name = "registration_time")
    private LocalDateTime registrationTime;

    @Column(name = "supported_capabilities", nullable = false, length = 1000)
    private String capabilities;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Agent() {}

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
        if (registrationTime == null) registrationTime = LocalDateTime.now();
        if (lastHeartbeat == null) lastHeartbeat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String name;
        private String description;
        private String endpoint;
        private String walletAddress;
        private String version = "1.0.0";
        private Double rating = 4.5;
        private Double successRate = 95.0;
        private Double basePrice = 50.0;
        private HealthStatus healthStatus = HealthStatus.ONLINE;
        private Double healthScore = 100.0;
        private Double averageResponseTime = 50.0;
        private Integer runningTasks = 0;
        private Integer maxConcurrency = 5;
        private Double currentLoad = 0.0;
        private Double cpuUsage = 0.0;
        private Double memoryUsage = 0.0;
        private Integer queueSize = 0;
        private LocalDateTime lastHeartbeat;
        private Long completedTasks = 0L;
        private Long failedTasks = 0L;
        private Integer peakConcurrency = 0;
        private LocalDateTime registrationTime;
        private String capabilities;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder endpoint(String endpoint) { this.endpoint = endpoint; return this; }
        public Builder walletAddress(String walletAddress) { this.walletAddress = walletAddress; return this; }
        public Builder version(String version) { this.version = version; return this; }
        public Builder rating(Double rating) { this.rating = rating; return this; }
        public Builder successRate(Double successRate) { this.successRate = successRate; return this; }
        public Builder basePrice(Double basePrice) { this.basePrice = basePrice; return this; }
        public Builder healthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; return this; }
        public Builder healthStatus(String statusStr) {
            if (statusStr != null) {
                try {
                    this.healthStatus = HealthStatus.valueOf(statusStr.toUpperCase());
                } catch (Exception e) {
                    this.healthStatus = HealthStatus.ONLINE;
                }
            }
            return this;
        }
        public Builder healthScore(Double healthScore) { this.healthScore = healthScore; return this; }
        public Builder averageResponseTime(Double averageResponseTime) { this.averageResponseTime = averageResponseTime; return this; }
        public Builder runningTasks(Integer runningTasks) { this.runningTasks = runningTasks; return this; }
        public Builder maxConcurrency(Integer maxConcurrency) { this.maxConcurrency = maxConcurrency; return this; }
        public Builder currentLoad(Double currentLoad) { this.currentLoad = currentLoad; return this; }
        public Builder cpuUsage(Double cpuUsage) { this.cpuUsage = cpuUsage; return this; }
        public Builder memoryUsage(Double memoryUsage) { this.memoryUsage = memoryUsage; return this; }
        public Builder queueSize(Integer queueSize) { this.queueSize = queueSize; return this; }
        public Builder lastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; return this; }
        public Builder completedTasks(Long completedTasks) { this.completedTasks = completedTasks; return this; }
        public Builder failedTasks(Long failedTasks) { this.failedTasks = failedTasks; return this; }
        public Builder peakConcurrency(Integer peakConcurrency) { this.peakConcurrency = peakConcurrency; return this; }
        public Builder registrationTime(LocalDateTime registrationTime) { this.registrationTime = registrationTime; return this; }
        public Builder capabilities(String capabilities) { this.capabilities = capabilities; return this; }
        public Builder supportedCapabilities(String capabilities) { this.capabilities = capabilities; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Agent build() {
            Agent agent = new Agent();
            agent.setId(id);
            agent.setName(name);
            agent.setDescription(description);
            agent.setEndpoint(endpoint);
            agent.setWalletAddress(walletAddress);
            if (version != null) agent.setVersion(version);
            if (rating != null) agent.setRating(rating);
            if (successRate != null) agent.setSuccessRate(successRate);
            if (basePrice != null) agent.setBasePrice(basePrice);
            if (healthStatus != null) agent.setHealthStatus(healthStatus);
            if (healthScore != null) agent.setHealthScore(healthScore);
            if (averageResponseTime != null) agent.setAverageResponseTime(averageResponseTime);
            if (runningTasks != null) agent.setRunningTasks(runningTasks);
            if (maxConcurrency != null) agent.setMaxConcurrency(maxConcurrency);
            if (currentLoad != null) agent.setCurrentLoad(currentLoad);
            if (cpuUsage != null) agent.setCpuUsage(cpuUsage);
            if (memoryUsage != null) agent.setMemoryUsage(memoryUsage);
            if (queueSize != null) agent.setQueueSize(queueSize);
            agent.setLastHeartbeat(lastHeartbeat != null ? lastHeartbeat : LocalDateTime.now());
            if (completedTasks != null) agent.setCompletedTasks(completedTasks);
            if (failedTasks != null) agent.setFailedTasks(failedTasks);
            if (peakConcurrency != null) agent.setPeakConcurrency(peakConcurrency);
            agent.setRegistrationTime(registrationTime != null ? registrationTime : LocalDateTime.now());
            agent.setCapabilities(capabilities);
            agent.setCreatedAt(createdAt);
            agent.setUpdatedAt(updatedAt);
            return agent;
        }
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
    public HealthStatus getHealthStatusEnum() { return healthStatus; }
    public String getHealthStatus() { return healthStatus != null ? healthStatus.name() : "ONLINE"; }
    public void setHealthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; }
    public void setHealthStatus(String statusStr) {
        if (statusStr != null) {
            try {
                this.healthStatus = HealthStatus.valueOf(statusStr.toUpperCase());
            } catch (Exception e) {
                this.healthStatus = HealthStatus.ONLINE;
            }
        }
    }
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
    public Integer getPeakConcurrency() { return peakConcurrency; }
    public void setPeakConcurrency(Integer peakConcurrency) { this.peakConcurrency = peakConcurrency; }
    public LocalDateTime getRegistrationTime() { return registrationTime; }
    public void setRegistrationTime(LocalDateTime registrationTime) { this.registrationTime = registrationTime; }
    public String getCapabilities() { return capabilities; }
    public void setCapabilities(String capabilities) { this.capabilities = capabilities; }
    public String getSupportedCapabilities() { return capabilities; }
    public void setSupportedCapabilities(String capabilities) { this.capabilities = capabilities; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
