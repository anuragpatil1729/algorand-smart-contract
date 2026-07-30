package com.agentmesh.router.x402.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.UUID;

public class X402Challenge {

    @JsonProperty("challengeId")
    private String challengeId;

    @JsonProperty("workflowId")
    private String workflowId;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("asset")
    private String asset = "USDC";

    @JsonProperty("assetId")
    private String assetId = "31566704"; // Algorand Testnet USDC ASA ID

    @JsonProperty("network")
    private String network = "algorand-testnet";

    @JsonProperty("merchantWallet")
    private String merchantWallet;

    @JsonProperty("facilitatorUrl")
    private String facilitatorUrl = "https://facilitator.goplausible.xyz";

    @JsonProperty("expiresAt")
    private Long expiresAt;

    @JsonProperty("requirements")
    private Map<String, Object> requirements;

    @JsonProperty("challengeMetadata")
    private Map<String, Object> challengeMetadata;

    public X402Challenge() {
        this.challengeId = "ch-" + UUID.randomUUID().toString().substring(0, 8);
        this.expiresAt = System.currentTimeMillis() + (300 * 1000L); // 5 minutes expiration
    }

    public X402Challenge(String workflowId, Double price, String merchantWallet) {
        this();
        this.workflowId = workflowId;
        this.price = price;
        this.merchantWallet = merchantWallet;
    }

    public String getChallengeId() { return challengeId; }
    public void setChallengeId(String challengeId) { this.challengeId = challengeId; }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getAsset() { return asset; }
    public void setAsset(String asset) { this.asset = asset; }

    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }

    public String getNetwork() { return network; }
    public void setNetwork(String network) { this.network = network; }

    public String getMerchantWallet() { return merchantWallet; }
    public void setMerchantWallet(String merchantWallet) { this.merchantWallet = merchantWallet; }

    public String getFacilitatorUrl() { return facilitatorUrl; }
    public void setFacilitatorUrl(String facilitatorUrl) { this.facilitatorUrl = facilitatorUrl; }

    public Long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Long expiresAt) { this.expiresAt = expiresAt; }

    public Map<String, Object> getRequirements() { return requirements; }
    public void setRequirements(Map<String, Object> requirements) { this.requirements = requirements; }

    public Map<String, Object> getChallengeMetadata() { return challengeMetadata; }
    public void setChallengeMetadata(Map<String, Object> challengeMetadata) { this.challengeMetadata = challengeMetadata; }
}
