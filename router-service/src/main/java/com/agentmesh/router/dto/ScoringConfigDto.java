package com.agentmesh.router.dto;

public class ScoringConfigDto {
    private Double reputationWeight;
    private Double successRateWeight;
    private Double confidenceWeight;
    private Double priceWeight;
    private Double etaWeight;

    public ScoringConfigDto() {}

    public ScoringConfigDto(Double reputationWeight, Double successRateWeight, Double confidenceWeight, Double priceWeight, Double etaWeight) {
        this.reputationWeight = reputationWeight;
        this.successRateWeight = successRateWeight;
        this.confidenceWeight = confidenceWeight;
        this.priceWeight = priceWeight;
        this.etaWeight = etaWeight;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Double reputationWeight;
        private Double successRateWeight;
        private Double confidenceWeight;
        private Double priceWeight;
        private Double etaWeight;

        public Builder reputationWeight(Double reputationWeight) { this.reputationWeight = reputationWeight; return this; }
        public Builder successRateWeight(Double successRateWeight) { this.successRateWeight = successRateWeight; return this; }
        public Builder confidenceWeight(Double confidenceWeight) { this.confidenceWeight = confidenceWeight; return this; }
        public Builder priceWeight(Double priceWeight) { this.priceWeight = priceWeight; return this; }
        public Builder etaWeight(Double etaWeight) { this.etaWeight = etaWeight; return this; }

        public ScoringConfigDto build() {
            return new ScoringConfigDto(reputationWeight, successRateWeight, confidenceWeight, priceWeight, etaWeight);
        }
    }

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
}
