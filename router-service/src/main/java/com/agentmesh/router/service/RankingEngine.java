package com.agentmesh.router.service;

import com.agentmesh.router.model.Agent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingEngine {

    private double qualityWeight = 0.35;
    private double reputationWeight = 0.25;
    private double latencyWeight = 0.20;
    private double priceWeight = 0.20;

    public RankingEngine() {}

    public RankingEngine(double qualityWeight, double reputationWeight, double latencyWeight, double priceWeight) {
        this.qualityWeight = qualityWeight;
        this.reputationWeight = reputationWeight;
        this.latencyWeight = latencyWeight;
        this.priceWeight = priceWeight;
    }

    public double calculateScore(Agent agent, List<Agent> pool) {
        if (agent == null) return 0.0;
        if (pool == null || pool.isEmpty()) pool = List.of(agent);

        // 1. Quality Component (Success Rate 0-100%)
        double qualityScore = agent.getSuccessRate() != null ? agent.getSuccessRate() : 95.0;

        // 2. Reputation Component (Rating out of 5 -> 0-100)
        double reputationScore = (agent.getRating() != null ? agent.getRating() : 4.5) * 20.0;

        // 3. Latency Component (Relative response time, 100 if fastest)
        double minLatency = pool.stream().mapToDouble(a -> a.getAverageResponseTime() != null ? a.getAverageResponseTime() : 500.0).min().orElse(100.0);
        double maxLatency = pool.stream().mapToDouble(a -> a.getAverageResponseTime() != null ? a.getAverageResponseTime() : 500.0).max().orElse(1000.0);
        double agentLatency = agent.getAverageResponseTime() != null ? agent.getAverageResponseTime() : 500.0;

        double latencyScore = 100.0;
        if (maxLatency > minLatency) {
            latencyScore = 100.0 - (((agentLatency - minLatency) / (maxLatency - minLatency)) * 50.0);
        }

        // 4. Price Component (Relative cost, 100 if cheapest)
        double minPrice = pool.stream().mapToDouble(a -> a.getBasePrice() != null ? a.getBasePrice() : 50.0).min().orElse(10.0);
        double maxPrice = pool.stream().mapToDouble(a -> a.getBasePrice() != null ? a.getBasePrice() : 50.0).max().orElse(100.0);
        double agentPrice = agent.getBasePrice() != null ? agent.getBasePrice() : 50.0;

        double priceScore = 100.0;
        if (maxPrice > minPrice) {
            priceScore = 100.0 - (((agentPrice - minPrice) / (maxPrice - minPrice)) * 50.0);
        }

        // Score = 0.35 * Quality + 0.25 * Reputation + 0.20 * Latency + 0.20 * Price
        double finalScore = (qualityScore * qualityWeight) +
                (reputationScore * reputationWeight) +
                (latencyScore * latencyWeight) +
                (priceScore * priceWeight);

        return Math.round(finalScore * 100.0) / 100.0;
    }

    public double getQualityWeight() { return qualityWeight; }
    public void setQualityWeight(double qualityWeight) { this.qualityWeight = qualityWeight; }
    public double getReputationWeight() { return reputationWeight; }
    public void setReputationWeight(double reputationWeight) { this.reputationWeight = reputationWeight; }
    public double getLatencyWeight() { return latencyWeight; }
    public void setLatencyWeight(double latencyWeight) { this.latencyWeight = latencyWeight; }
    public double getPriceWeight() { return priceWeight; }
    public void setPriceWeight(double priceWeight) { this.priceWeight = priceWeight; }
}
