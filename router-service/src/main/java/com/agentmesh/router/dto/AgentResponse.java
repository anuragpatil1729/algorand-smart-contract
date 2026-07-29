package com.agentmesh.router.dto;

import com.agentmesh.router.model.enums.HealthStatus;
import java.time.LocalDateTime;
import java.util.List;

public class AgentResponse {
    private String id;
    private String name;
    private String description;
    private String endpoint;
    private String walletAddress;
    private Double rating;
    private Double successRate;
    private Double basePrice;
    private HealthStatus healthStatus;
    private List<String> capabilities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AgentResponse() {}

    public AgentResponse(String id, String name, String description, String endpoint, String walletAddress, Double rating, Double successRate, Double basePrice, HealthStatus healthStatus, List<String> capabilities, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.endpoint = endpoint;
        this.walletAddress = walletAddress;
        this.rating = rating;
        this.successRate = successRate;
        this.basePrice = basePrice;
        this.healthStatus = healthStatus;
        this.capabilities = capabilities;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }
    public HealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; }
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
