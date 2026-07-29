package com.agentmesh.router.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AgentRegistrationRequestDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("walletAddress")
    private String walletAddress;

    @JsonProperty("version")
    private String version = "1.0.0";

    @JsonProperty("capabilities")
    private List<String> capabilities;

    @JsonProperty("supportedTaskTypes")
    private List<String> supportedTaskTypes;

    @JsonProperty("maxConcurrency")
    private Integer maxConcurrency = 5;

    @JsonProperty("basePrice")
    private Double basePrice = 50.0;

    public AgentRegistrationRequestDto() {}

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
    public List<String> getCapabilities() { return capabilities; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
    public List<String> getSupportedTaskTypes() { return supportedTaskTypes; }
    public void setSupportedTaskTypes(List<String> supportedTaskTypes) { this.supportedTaskTypes = supportedTaskTypes; }
    public Integer getMaxConcurrency() { return maxConcurrency; }
    public void setMaxConcurrency(Integer maxConcurrency) { this.maxConcurrency = maxConcurrency; }
    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }
}
