package com.agentmesh.router.quote.strategy;

import com.agentmesh.router.quote.ScoringEngine;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;

import java.util.List;

public interface SelectionStrategy {

    String getStrategyName();

    AgentQuoteResponse selectAgent(List<AgentQuoteResponse> quotes, ScoringEngine scoringEngine, ScoringEngine.Weights customWeights);

    List<AgentQuoteResponse> rankAgents(List<AgentQuoteResponse> quotes, ScoringEngine scoringEngine, ScoringEngine.Weights customWeights);
}
