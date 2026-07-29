package com.agentmesh.router.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class AgentRequest {

    @NotBlank(message = "Agent name is required")
    private String name;

    private String description;

    @NotBlank(message = "Endpoint URL is required")
    private String endpoint;

    @NotBlank(message = "Algorand wallet address is required")
    private String walletAddress;

    @NotNull(message = "Base price is required")
    @Min(value = 0, message = "Base price must be non-negative")
    private Double basePrice;

    private List<String> capabilities;

    public AgentRequest() {}

    public AgentRequest(String name, String description, String endpoint, String walletAddress, Double basePrice, List<String> capabilities) {
        this.name = name;
        this.description = description;
        this.endpoint = endpoint;
        this.walletAddress = walletAddress;
        this.basePrice = basePrice;
        this.capabilities = capabilities;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getWalletAddress() { return walletAddress; }
    public void setWalletAddress(String walletAddress) { this.walletAddress = walletAddress; }
    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
}
