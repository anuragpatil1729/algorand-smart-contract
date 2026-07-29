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

    @Column(name = "wallet_address", nullable = false)
    private String walletAddress;

    private Double rating = 4.5;

    @Column(name = "success_rate")
    private Double successRate = 95.0;

    @Column(name = "base_price")
    private Double basePrice = 50.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status", nullable = false)
    private HealthStatus healthStatus = HealthStatus.UP;

    @Column(nullable = false, length = 1000)
    private String capabilities;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Agent() {}

    public Agent(String id, String name, String description, String endpoint, String walletAddress, Double rating, Double successRate, Double basePrice, HealthStatus healthStatus, String capabilities, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.endpoint = endpoint;
        this.walletAddress = walletAddress;
        this.rating = rating != null ? rating : 4.5;
        this.successRate = successRate != null ? successRate : 95.0;
        this.basePrice = basePrice != null ? basePrice : 50.0;
        this.healthStatus = healthStatus != null ? healthStatus : HealthStatus.UP;
        this.capabilities = capabilities;
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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String name;
        private String description;
        private String endpoint;
        private String walletAddress;
        private Double rating = 4.5;
        private Double successRate = 95.0;
        private Double basePrice = 50.0;
        private HealthStatus healthStatus = HealthStatus.UP;
        private String capabilities;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder endpoint(String endpoint) { this.endpoint = endpoint; return this; }
        public Builder walletAddress(String walletAddress) { this.walletAddress = walletAddress; return this; }
        public Builder rating(Double rating) { this.rating = rating; return this; }
        public Builder successRate(Double successRate) { this.successRate = successRate; return this; }
        public Builder basePrice(Double basePrice) { this.basePrice = basePrice; return this; }
        public Builder healthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; return this; }
        public Builder healthStatus(String statusStr) {
            if (statusStr != null) {
                try {
                    this.healthStatus = HealthStatus.valueOf(statusStr.toUpperCase());
                } catch (Exception e) {
                    this.healthStatus = HealthStatus.UP;
                }
            }
            return this;
        }

        public Builder capabilities(String capabilities) { this.capabilities = capabilities; return this; }
        public Builder supportedCapabilities(String capabilities) { this.capabilities = capabilities; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Agent build() {
            return new Agent(id, name, description, endpoint, walletAddress, rating, successRate, basePrice, healthStatus, capabilities, createdAt, updatedAt);
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
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }

    public HealthStatus getHealthStatusEnum() { return healthStatus; }
    public String getHealthStatus() { return healthStatus != null ? healthStatus.name() : "UP"; }
    public void setHealthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; }
    public void setHealthStatus(String statusStr) {
        if (statusStr != null) {
            try {
                this.healthStatus = HealthStatus.valueOf(statusStr.toUpperCase());
            } catch (Exception e) {
                this.healthStatus = HealthStatus.UP;
            }
        }
    }

    public String getCapabilities() { return capabilities; }
    public void setCapabilities(String capabilities) { this.capabilities = capabilities; }
    public String getSupportedCapabilities() { return capabilities; }
    public void setSupportedCapabilities(String capabilities) { this.capabilities = capabilities; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
