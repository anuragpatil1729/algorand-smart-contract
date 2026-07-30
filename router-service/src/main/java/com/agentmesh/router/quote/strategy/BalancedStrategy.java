package com.agentmesh.router.quote.strategy;

import com.agentmesh.router.quote.ScoringEngine;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BalancedStrategy implements SelectionStrategy {

    @Override
    public String getStrategyName() {
        return "BALANCED";
    }

    @Override
    public List<AgentQuoteResponse> rankAgents(List<AgentQuoteResponse> quotes, ScoringEngine scoringEngine, ScoringEngine.Weights customWeights) {
        if (quotes == null || quotes.isEmpty()) return Collections.emptyList();

        List<AgentQuoteResponse> validQuotes = quotes.stream()
                .filter(q -> Boolean.TRUE.equals(q.getValid()))
                .collect(Collectors.toList());

        if (validQuotes.isEmpty()) return Collections.emptyList();

        for (AgentQuoteResponse quote : validQuotes) {
            double score = scoringEngine.calculateScore(quote, validQuotes, customWeights);
            quote.setScore(score);
        }

        validQuotes.sort(Comparator.comparingDouble(AgentQuoteResponse::getScore).reversed());
        return validQuotes;
    }

    @Override
    public AgentQuoteResponse selectAgent(List<AgentQuoteResponse> quotes, ScoringEngine scoringEngine, ScoringEngine.Weights customWeights) {
        List<AgentQuoteResponse> ranked = rankAgents(quotes, scoringEngine, customWeights);
        return ranked.isEmpty() ? null : ranked.get(0);
    }
}
