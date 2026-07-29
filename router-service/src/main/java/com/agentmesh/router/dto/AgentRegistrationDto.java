package com.agentmesh.router.dto;

import java.util.List;

public class AgentRegistrationDto {
    private String id;
    private String name;
    private String endpoint;
    private String walletAddress;
    private Double rating;
    private Double successRate;
    private Double basePrice;
    private List<String> supportedCapabilities;

    public AgentRegistrationDto() {}

    public AgentRegistrationDto(String id, String name, String endpoint, String walletAddress, Double rating, Double successRate, Double basePrice, List<String> supportedCapabilities) {
        this.id = id;
        this.name = name;
        this.endpoint = endpoint;
        this.walletAddress = walletAddress;
        this.rating = rating;
        this.successRate = successRate;
        this.basePrice = basePrice;
        this.supportedCapabilities = supportedCapabilities;
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
    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }
    public List<String> getSupportedCapabilities() { return supportedCapabilities; }
    public void setSupportedCapabilities(List<String> supportedCapabilities) { this.supportedCapabilities = supportedCapabilities; }
}
