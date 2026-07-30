package com.agentmesh.router.quote;

import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("quoteScoringEngine")
public class ScoringEngine {

    public static class Weights {
        private double reputationWeight = 0.35;
        private double healthWeight = 0.20;
        private double confidenceWeight = 0.15;
        private double etaWeight = 0.10;
        private double loadWeight = 0.10;
        private double priceWeight = 0.10;

        public Weights() {}

        public Weights(double reputationWeight, double healthWeight, double confidenceWeight,
                       double etaWeight, double loadWeight, double priceWeight) {
            this.reputationWeight = reputationWeight;
            this.healthWeight = healthWeight;
            this.confidenceWeight = confidenceWeight;
            this.etaWeight = etaWeight;
            this.loadWeight = loadWeight;
            this.priceWeight = priceWeight;
        }

        public double getReputationWeight() { return reputationWeight; }
        public void setReputationWeight(double reputationWeight) { this.reputationWeight = reputationWeight; }

        public double getHealthWeight() { return healthWeight; }
        public void setHealthWeight(double healthWeight) { this.healthWeight = healthWeight; }

        public double getConfidenceWeight() { return confidenceWeight; }
        public void setConfidenceWeight(double confidenceWeight) { this.confidenceWeight = confidenceWeight; }

        public double getEtaWeight() { return etaWeight; }
        public void setEtaWeight(double etaWeight) { this.etaWeight = etaWeight; }

        public double getLoadWeight() { return loadWeight; }
        public void setLoadWeight(double loadWeight) { this.loadWeight = loadWeight; }

        public double getPriceWeight() { return priceWeight; }
        public void setPriceWeight(double priceWeight) { this.priceWeight = priceWeight; }
    }

    private Weights defaultWeights = new Weights();

    public ScoringEngine() {}

    public ScoringEngine(Weights weights) {
        if (weights != null) this.defaultWeights = weights;
    }

    public Weights getDefaultWeights() { return defaultWeights; }
    public void setDefaultWeights(Weights defaultWeights) { this.defaultWeights = defaultWeights; }

    public double calculateScore(AgentQuoteResponse quote, List<AgentQuoteResponse> allQuotesForTask) {
        return calculateScore(quote, allQuotesForTask, defaultWeights);
    }

    public double calculateScore(AgentQuoteResponse quote, List<AgentQuoteResponse> allQuotesForTask, Weights weights) {
        if (quote == null || !Boolean.TRUE.equals(quote.getValid())) {
            return 0.0;
        }
        if (weights == null) weights = defaultWeights;
        if (allQuotesForTask == null || allQuotesForTask.isEmpty()) {
            allQuotesForTask = List.of(quote);
        }

        double minPrice = allQuotesForTask.stream()
                .filter(q -> Boolean.TRUE.equals(q.getValid()))
                .mapToDouble(AgentQuoteResponse::getQuotedPrice)
                .min().orElse(quote.getQuotedPrice());
        double maxPrice = allQuotesForTask.stream()
                .filter(q -> Boolean.TRUE.equals(q.getValid()))
                .mapToDouble(AgentQuoteResponse::getQuotedPrice)
                .max().orElse(quote.getQuotedPrice());

        double minEta = allQuotesForTask.stream()
                .filter(q -> Boolean.TRUE.equals(q.getValid()))
                .mapToDouble(AgentQuoteResponse::getEstimatedDuration)
                .min().orElse(quote.getEstimatedDuration());
        double maxEta = allQuotesForTask.stream()
                .filter(q -> Boolean.TRUE.equals(q.getValid()))
                .mapToDouble(AgentQuoteResponse::getEstimatedDuration)
                .max().orElse(quote.getEstimatedDuration());

        double minLoad = allQuotesForTask.stream()
                .filter(q -> Boolean.TRUE.equals(q.getValid()))
                .mapToDouble(AgentQuoteResponse::getCurrentLoad)
                .min().orElse(quote.getCurrentLoad());
        double maxLoad = allQuotesForTask.stream()
                .filter(q -> Boolean.TRUE.equals(q.getValid()))
                .mapToDouble(AgentQuoteResponse::getCurrentLoad)
                .max().orElse(quote.getCurrentLoad());

        // Normalized reputation (0-100)
        double repScore = (quote.getReputation() / 5.0) * 100.0;
        repScore = Math.min(100.0, Math.max(0.0, repScore));

        // Health score (0-100)
        double healthScore = quote.getHealthScore() != null ? quote.getHealthScore() : 100.0;
        healthScore = Math.min(100.0, Math.max(0.0, healthScore));

        // Confidence (0-100)
        double confScore = quote.getConfidence() != null ? quote.getConfidence() : 95.0;
        confScore = Math.min(100.0, Math.max(0.0, confScore));

        // Relative Price score (100 if cheapest)
        double priceScore = 100.0;
        if (maxPrice > minPrice) {
            priceScore = 100.0 - (((quote.getQuotedPrice() - minPrice) / (maxPrice - minPrice)) * 50.0);
        }

        // Relative ETA score (100 if fastest)
        double etaScore = 100.0;
        if (maxEta > minEta) {
            etaScore = 100.0 - (((quote.getEstimatedDuration() - minEta) / (maxEta - minEta)) * 50.0);
        }

        // Relative Load score (100 if least loaded)
        double loadScore = 100.0;
        if (maxLoad > minLoad) {
            loadScore = 100.0 - (((quote.getCurrentLoad() - minLoad) / (maxLoad - minLoad)) * 50.0);
        }

        double totalWeight = weights.getReputationWeight() + weights.getHealthWeight() +
                weights.getConfidenceWeight() + weights.getEtaWeight() +
                weights.getLoadWeight() + weights.getPriceWeight();

        if (totalWeight <= 0.0) totalWeight = 1.0;

        double weightedSum = (weights.getReputationWeight() * repScore) +
                (weights.getHealthWeight() * healthScore) +
                (weights.getConfidenceWeight() * confScore) +
                (weights.getEtaWeight() * etaScore) +
                (weights.getLoadWeight() * loadScore) +
                (weights.getPriceWeight() * priceScore);

        double finalScore = Math.round((weightedSum / totalWeight) * 100.0) / 100.0;
        quote.setScore(finalScore);
        return finalScore;
    }

    public Weights parseWeightsMap(Map<String, Double> map) {
        if (map == null || map.isEmpty()) return defaultWeights;
        Weights w = new Weights();
        if (map.containsKey("reputation")) w.setReputationWeight(map.get("reputation"));
        if (map.containsKey("health")) w.setHealthWeight(map.get("health"));
        if (map.containsKey("confidence")) w.setConfidenceWeight(map.get("confidence"));
        if (map.containsKey("eta") || map.containsKey("duration")) w.setEtaWeight(map.getOrDefault("eta", map.get("duration")));
        if (map.containsKey("load")) w.setLoadWeight(map.get("load"));
        if (map.containsKey("price")) w.setPriceWeight(map.get("price"));
        return w;
    }
}
