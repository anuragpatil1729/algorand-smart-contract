package com.agentmesh.router.quote;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import com.agentmesh.router.quote.dto.TaskAssignment;
import com.agentmesh.router.quote.strategy.BalancedStrategy;
import com.agentmesh.router.quote.strategy.SelectionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AgentSelector {

    private static final Logger log = LoggerFactory.getLogger(AgentSelector.class);

    private final ScoringEngine scoringEngine;
    private final AgentDiscoveryService discoveryService;
    private final Map<String, SelectionStrategy> strategyMap;
    private final BalancedStrategy defaultStrategy;

    public AgentSelector(ScoringEngine scoringEngine, AgentDiscoveryService discoveryService, List<SelectionStrategy> strategies, BalancedStrategy defaultStrategy) {
        this.scoringEngine = scoringEngine;
        this.discoveryService = discoveryService;
        this.defaultStrategy = defaultStrategy;
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        s -> s.getStrategyName().toUpperCase(),
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }

    public TaskAssignment selectAgentForTask(PlannedTaskDto task, List<AgentQuoteResponse> candidateQuotes, String strategyName, ScoringEngine.Weights customWeights) {
        String resolvedStrategy = (strategyName != null && !strategyName.isBlank()) ? strategyName.toUpperCase() : "BALANCED";
        SelectionStrategy strategy = strategyMap.getOrDefault(resolvedStrategy, defaultStrategy);

        List<AgentQuoteResponse> rankedQuotes = strategy.rankAgents(candidateQuotes, scoringEngine, customWeights);

        TaskAssignment assignment = new TaskAssignment();
        assignment.setTaskId(task.getTaskId());
        assignment.setTaskName(task.getTaskName());
        assignment.setRequiredCapability(task.getRequiredCapability());

        if (rankedQuotes.isEmpty()) {
            assignment.setSelectionReason("No valid candidate quotes received for capability: " + task.getRequiredCapability());
            log.warn("No candidate agent quotes available for task '{}'", task.getTaskId());
            return assignment;
        }

        AgentQuoteResponse selected = rankedQuotes.get(0);
        assignment.setSelectedAgentId(selected.getAgentId());
        assignment.setSelectedAgentName(selected.getAgentName());
        assignment.setQuotedPrice(selected.getQuotedPrice());
        assignment.setEstimatedDuration(selected.getEstimatedDuration());

        // Resolve endpoint from discovery service if available
        List<Agent> agents = discoveryService.discoverAllAgents();
        Optional<Agent> agentObj = agents.stream().filter(a -> a.getId().equals(selected.getAgentId())).findFirst();
        agentObj.ifPresent(a -> assignment.setSelectedAgentEndpoint(a.getEndpoint()));

        // Alternatives (runner-ups)
        List<AgentQuoteResponse> alternatives = new ArrayList<>();
        for (int i = 1; i < rankedQuotes.size(); i++) {
            alternatives.add(rankedQuotes.get(i));
        }
        assignment.setAlternativeAgents(alternatives);

        // Build detailed selection reason
        String reason = String.format("Selected agent '%s' (%s) using %s strategy. Score: %.2f, Price: $%.2f, Duration: %ds, Reputation: %.2f",
                selected.getAgentName(), selected.getAgentId(), strategy.getStrategyName(),
                selected.getScore() != null ? selected.getScore() : 0.0,
                selected.getQuotedPrice(), selected.getEstimatedDuration(), selected.getReputation());
        assignment.setSelectionReason(reason);

        return assignment;
    }

    public SelectionStrategy resolveStrategy(String strategyName) {
        if (strategyName == null || strategyName.isBlank()) return defaultStrategy;
        return strategyMap.getOrDefault(strategyName.toUpperCase(), defaultStrategy);
    }
}
