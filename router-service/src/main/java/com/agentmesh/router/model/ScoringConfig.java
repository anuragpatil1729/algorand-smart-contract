package com.agentmesh.router.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scoring_config")
public class ScoringConfig {

    @Id
    private String id;

    @Column(name = "reputation_weight")
    private Double reputationWeight = 0.35;

    @Column(name = "success_rate_weight")
    private Double successRateWeight = 0.25;

    @Column(name = "confidence_weight")
    private Double confidenceWeight = 0.20;

    @Column(name = "price_weight")
    private Double priceWeight = 0.10;

    @Column(name = "eta_weight")
    private Double etaWeight = 0.10;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ScoringConfig() {}

    public ScoringConfig(String id, Double reputationWeight, Double successRateWeight, Double confidenceWeight, Double priceWeight, Double etaWeight, LocalDateTime updatedAt) {
        this.id = id;
        this.reputationWeight = reputationWeight != null ? reputationWeight : 0.35;
        this.successRateWeight = successRateWeight != null ? successRateWeight : 0.25;
        this.confidenceWeight = confidenceWeight != null ? confidenceWeight : 0.20;
        this.priceWeight = priceWeight != null ? priceWeight : 0.10;
        this.etaWeight = etaWeight != null ? etaWeight : 0.10;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    @PreUpdate
    protected void onSave() {
        updatedAt = LocalDateTime.now();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id = "DEFAULT";
        private Double reputationWeight = 0.35;
        private Double successRateWeight = 0.25;
        private Double confidenceWeight = 0.20;
        private Double priceWeight = 0.10;
        private Double etaWeight = 0.10;
        private LocalDateTime updatedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder reputationWeight(Double reputationWeight) { this.reputationWeight = reputationWeight; return this; }
        public Builder successRateWeight(Double successRateWeight) { this.successRateWeight = successRateWeight; return this; }
        public Builder confidenceWeight(Double confidenceWeight) { this.confidenceWeight = confidenceWeight; return this; }
        public Builder priceWeight(Double priceWeight) { this.priceWeight = priceWeight; return this; }
        public Builder etaWeight(Double etaWeight) { this.etaWeight = etaWeight; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ScoringConfig build() {
            return new ScoringConfig(id, reputationWeight, successRateWeight, confidenceWeight, priceWeight, etaWeight, updatedAt);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Double getReputationWeight() { return reputationWeight; }
    public void setReputationWeight(Double reputationWeight) { this.reputationWeight = reputationWeight; }
    public Double getSuccessRateWeight() { return successRateWeight; }
    public void setSuccessRateWeight(Double successRateWeight) { this.successRateWeight = successRateWeight; }
    public Double getConfidenceWeight() { return confidenceWeight; }
    public void setConfidenceWeight(Double confidenceWeight) { this.confidenceWeight = confidenceWeight; }
    public Double getPriceWeight() { return priceWeight; }
    public void setPriceWeight(Double priceWeight) { this.priceWeight = priceWeight; }
    public Double getEtaWeight() { return etaWeight; }
    public void setEtaWeight(Double etaWeight) { this.etaWeight = etaWeight; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
