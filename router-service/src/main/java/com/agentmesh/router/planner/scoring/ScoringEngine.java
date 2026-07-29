package com.agentmesh.router.planner.scoring;

import com.agentmesh.router.model.Quote;
import com.agentmesh.router.model.ScoringConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScoringEngine {

    public double calculateScore(Quote quote, List<Quote> allQuotesForTask, ScoringConfig config) {
        if (config == null) {
            config = ScoringConfig.builder()
                    .reputationWeight(0.35)
                    .successRateWeight(0.25)
                    .confidenceWeight(0.20)
                    .priceWeight(0.10)
                    .etaWeight(0.10)
                    .build();
        }

        double minPrice = allQuotesForTask.stream().mapToDouble(Quote::getPrice).min().orElse(quote.getPrice());
        double maxPrice = allQuotesForTask.stream().mapToDouble(Quote::getPrice).max().orElse(quote.getPrice());
        
        double minEta = allQuotesForTask.stream().mapToDouble(Quote::getEstimatedTimeSeconds).min().orElse(quote.getEstimatedTimeSeconds());
        double maxEta = allQuotesForTask.stream().mapToDouble(Quote::getEstimatedTimeSeconds).max().orElse(quote.getEstimatedTimeSeconds());

        // Normalized reputation (0-100)
        double repScore = (quote.getRating() / 5.0) * 100.0;
        
        // Success rate & confidence (0-100)
        double succScore = quote.getSuccessRate();
        double confScore = quote.getConfidence();

        // Price score: 100 if cheapest, scales down for higher price
        double priceScore = 100.0;
        if (maxPrice > minPrice) {
            priceScore = 100.0 - (((quote.getPrice() - minPrice) / (maxPrice - minPrice)) * 50.0);
        }

        // ETA score: 100 if fastest, scales down for slower ETA
        double etaScore = 100.0;
        if (maxEta > minEta) {
            etaScore = 100.0 - (((quote.getEstimatedTimeSeconds() - minEta) / (maxEta - minEta)) * 50.0);
        }

        double finalScore = (config.getReputationWeight() * repScore) +
                            (config.getSuccessRateWeight() * succScore) +
                            (config.getConfidenceWeight() * confScore) +
                            (config.getPriceWeight() * priceScore) +
                            (config.getEtaWeight() * etaScore);

        return Math.round(finalScore * 100.0) / 100.0;
    }
}
