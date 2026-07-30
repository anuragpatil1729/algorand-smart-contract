package com.agentmesh.router.quote.strategy;

import com.agentmesh.router.quote.ScoringEngine;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FastestCompletionStrategy implements SelectionStrategy {

    @Override
    public String getStrategyName() {
        return "FASTEST_COMPLETION";
    }

    @Override
    public List<AgentQuoteResponse> rankAgents(List<AgentQuoteResponse> quotes, ScoringEngine scoringEngine, ScoringEngine.Weights customWeights) {
        if (quotes == null || quotes.isEmpty()) return Collections.emptyList();

        List<AgentQuoteResponse> validQuotes = quotes.stream()
                .filter(q -> Boolean.TRUE.equals(q.getValid()))
                .collect(Collectors.toList());

        if (validQuotes.isEmpty()) return Collections.emptyList();

        ScoringEngine.Weights fastWeights = new ScoringEngine.Weights(0.10, 0.10, 0.10, 0.60, 0.05, 0.05);
        for (AgentQuoteResponse quote : validQuotes) {
            double score = scoringEngine.calculateScore(quote, validQuotes, fastWeights);
            quote.setScore(score);
        }

        validQuotes.sort(Comparator.comparingInt(AgentQuoteResponse::getEstimatedDuration));
        return validQuotes;
    }

    @Override
    public AgentQuoteResponse selectAgent(List<AgentQuoteResponse> quotes, ScoringEngine scoringEngine, ScoringEngine.Weights customWeights) {
        List<AgentQuoteResponse> ranked = rankAgents(quotes, scoringEngine, customWeights);
        return ranked.isEmpty() ? null : ranked.get(0);
    }
}
