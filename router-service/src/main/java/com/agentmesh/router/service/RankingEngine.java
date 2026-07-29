package com.agentmesh.router.service;

import com.agentmesh.router.model.Agent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingEngine {

    private double reputationWeight = 0.40;
    private double healthWeight = 0.20;
    private double responseTimeWeight = 0.15;
    private double loadWeight = 0.15;
    private double priceWeight = 0.10;

    public RankingEngine() {}

    public RankingEngine(double reputationWeight, double healthWeight, double responseTimeWeight, double loadWeight, double priceWeight) {
        this.reputationWeight = reputationWeight;
        this.healthWeight = healthWeight;
        this.responseTimeWeight = responseTimeWeight;
        this.loadWeight = loadWeight;
        this.priceWeight = priceWeight;
    }

    public double calculateScore(Agent agent, List<Agent> pool) {
        if (agent == null) return 0.0;

        // 1. Reputation Component (Rating out of 5 -> 0-100)
        double reputationScore = (agent.getRating() != null ? agent.getRating() : 4.0) * 20.0;

        // 2. Health Score Component (0-100)
        double healthScore = agent.getHealthScore() != null ? agent.getHealthScore() : 100.0;

        // 3. Response Time Component (Lower is better, scale 0-100)
        double responseTimeMs = agent.getAverageResponseTime() != null ? agent.getAverageResponseTime() : 50.0;
        double responseTimeScore = Math.max(0.0, 100.0 - (responseTimeMs / 5.0));

        // 4. Current Load Component (Lower is better, 0-100%)
        double load = agent.getCurrentLoad() != null ? agent.getCurrentLoad() : 0.0;
        double loadScore = Math.max(0.0, 100.0 - load);

        // 5. Price Component (Lower is better relative to max price in pool)
        double maxPrice = pool.stream()
                .mapToDouble(a -> a.getBasePrice() != null ? a.getBasePrice() : 50.0)
                .max()
                .orElse(100.0);
        double price = agent.getBasePrice() != null ? agent.getBasePrice() : 50.0;
        double priceScore = maxPrice > 0 ? Math.max(0.0, (1.0 - (price / (maxPrice * 1.2))) * 100.0) : 50.0;

        double finalScore = (reputationScore * reputationWeight) +
                (healthScore * healthWeight) +
                (responseTimeScore * responseTimeWeight) +
                (loadScore * loadWeight) +
                (priceScore * priceWeight);

        return Math.round(finalScore * 100.0) / 100.0;
    }

    public double getReputationWeight() { return reputationWeight; }
    public void setReputationWeight(double reputationWeight) { this.reputationWeight = reputationWeight; }
    public double getHealthWeight() { return healthWeight; }
    public void setHealthWeight(double healthWeight) { this.healthWeight = healthWeight; }
    public double getResponseTimeWeight() { return responseTimeWeight; }
    public void setResponseTimeWeight(double responseTimeWeight) { this.responseTimeWeight = responseTimeWeight; }
    public double getLoadWeight() { return loadWeight; }
    public void setLoadWeight(double loadWeight) { this.loadWeight = loadWeight; }
    public double getPriceWeight() { return priceWeight; }
    public void setPriceWeight(double priceWeight) { this.priceWeight = priceWeight; }
}
