package com.agentmesh.router.dto;

import java.util.List;

public class AgentDto {
    private String id;
    private String name;
    private String endpoint;
    private String walletAddress;
    private Double rating;
    private Double successRate;
    private String healthStatus;
    private Double basePrice;
    private List<String> supportedCapabilities;

    public AgentDto() {}

    public AgentDto(String id, String name, String endpoint, String walletAddress, Double rating, Double successRate, String healthStatus, Double basePrice, List<String> supportedCapabilities) {
        this.id = id;
        this.name = name;
        this.endpoint = endpoint;
        this.walletAddress = walletAddress;
        this.rating = rating;
        this.successRate = successRate;
        this.healthStatus = healthStatus;
        this.basePrice = basePrice;
        this.supportedCapabilities = supportedCapabilities;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String name;
        private String endpoint;
        private String walletAddress;
        private Double rating;
        private Double successRate;
        private String healthStatus;
        private Double basePrice;
        private List<String> supportedCapabilities;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder endpoint(String endpoint) { this.endpoint = endpoint; return this; }
        public Builder walletAddress(String walletAddress) { this.walletAddress = walletAddress; return this; }
        public Builder rating(Double rating) { this.rating = rating; return this; }
        public Builder successRate(Double successRate) { this.successRate = successRate; return this; }
        public Builder healthStatus(String healthStatus) { this.healthStatus = healthStatus; return this; }
        public Builder basePrice(Double basePrice) { this.basePrice = basePrice; return this; }
        public Builder supportedCapabilities(List<String> supportedCapabilities) { this.supportedCapabilities = supportedCapabilities; return this; }

        public AgentDto build() {
            return new AgentDto(id, name, endpoint, walletAddress, rating, successRate, healthStatus, basePrice, supportedCapabilities);
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
    public List<String> getSupportedCapabilities() { return supportedCapabilities; }
    public void setSupportedCapabilities(List<String> supportedCapabilities) { this.supportedCapabilities = supportedCapabilities; }
}
