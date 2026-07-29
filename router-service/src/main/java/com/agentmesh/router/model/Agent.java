package com.agentmesh.router.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agents")
public class Agent {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String endpoint;

    @Column(name = "wallet_address", nullable = false)
    private String walletAddress;

    private Double rating = 4.5;

    @Column(name = "success_rate")
    private Double successRate = 95.0;

    @Column(name = "health_status")
    private String healthStatus = "UP";

    @Column(name = "base_price")
    private Double basePrice = 50.0;

    @Column(name = "supported_capabilities", nullable = false, length = 1000)
    private String supportedCapabilities;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Agent() {}

    public Agent(String id, String name, String endpoint, String walletAddress, Double rating, Double successRate, String healthStatus, Double basePrice, String supportedCapabilities, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.endpoint = endpoint;
        this.walletAddress = walletAddress;
        this.rating = rating != null ? rating : 4.5;
        this.successRate = successRate != null ? successRate : 95.0;
        this.healthStatus = healthStatus != null ? healthStatus : "UP";
        this.basePrice = basePrice != null ? basePrice : 50.0;
        this.supportedCapabilities = supportedCapabilities;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String endpoint;
        private String walletAddress;
        private Double rating = 4.5;
        private Double successRate = 95.0;
        private String healthStatus = "UP";
        private Double basePrice = 50.0;
        private String supportedCapabilities;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder endpoint(String endpoint) { this.endpoint = endpoint; return this; }
        public Builder walletAddress(String walletAddress) { this.walletAddress = walletAddress; return this; }
        public Builder rating(Double rating) { this.rating = rating; return this; }
        public Builder successRate(Double successRate) { this.successRate = successRate; return this; }
        public Builder healthStatus(String healthStatus) { this.healthStatus = healthStatus; return this; }
        public Builder basePrice(Double basePrice) { this.basePrice = basePrice; return this; }
        public Builder supportedCapabilities(String supportedCapabilities) { this.supportedCapabilities = supportedCapabilities; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Agent build() {
            return new Agent(id, name, endpoint, walletAddress, rating, successRate, healthStatus, basePrice, supportedCapabilities, createdAt, updatedAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getWalletAddress() { return walletAddress; }
    public void setWalletAddress(String walletAddress) { this.walletAddress = walletAddress; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }
    public String getSupportedCapabilities() { return supportedCapabilities; }
    public void setSupportedCapabilities(String supportedCapabilities) { this.supportedCapabilities = supportedCapabilities; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
