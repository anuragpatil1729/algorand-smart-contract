package com.agentmesh.router.quote.strategy;

import com.agentmesh.router.quote.ScoringEngine;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class LowestPriceStrategy implements SelectionStrategy {

    @Override
    public String getStrategyName() {
        return "LOWEST_PRICE";
    }

    @Override
    public List<AgentQuoteResponse> rankAgents(List<AgentQuoteResponse> quotes, ScoringEngine scoringEngine, ScoringEngine.Weights customWeights) {
        if (quotes == null || quotes.isEmpty()) return Collections.emptyList();

        List<AgentQuoteResponse> validQuotes = quotes.stream()
                .filter(q -> Boolean.TRUE.equals(q.getValid()))
                .collect(Collectors.toList());

        if (validQuotes.isEmpty()) return Collections.emptyList();

        // High weight on price for scoring display
        ScoringEngine.Weights priceWeights = new ScoringEngine.Weights(0.10, 0.10, 0.05, 0.05, 0.05, 0.65);
        for (AgentQuoteResponse quote : validQuotes) {
            double score = scoringEngine.calculateScore(quote, validQuotes, priceWeights);
            quote.setScore(score);
        }

        validQuotes.sort(Comparator.comparingDouble(AgentQuoteResponse::getQuotedPrice));
        return validQuotes;
    }

    @Override
    public AgentQuoteResponse selectAgent(List<AgentQuoteResponse> quotes, ScoringEngine scoringEngine, ScoringEngine.Weights customWeights) {
        List<AgentQuoteResponse> ranked = rankAgents(quotes, scoringEngine, customWeights);
        return ranked.isEmpty() ? null : ranked.get(0);
    }
}
