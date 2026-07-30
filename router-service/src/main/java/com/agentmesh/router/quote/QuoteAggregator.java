package com.agentmesh.router.quote;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import com.agentmesh.router.quote.dto.TaskQuoteSummary;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class QuoteAggregator {

    private final ScoringEngine scoringEngine;

    public QuoteAggregator(ScoringEngine scoringEngine) {
        this.scoringEngine = scoringEngine;
    }

    public TaskQuoteSummary aggregateForTask(PlannedTaskDto task, List<AgentQuoteResponse> rawQuotes, ScoringEngine.Weights customWeights) {
        String taskId = task != null ? task.getTaskId() : "unknown-task";
        String taskName = task != null ? task.getTaskName() : "Task";
        String capability = task != null ? task.getRequiredCapability() : "ALL";

        TaskQuoteSummary summary = new TaskQuoteSummary(taskId, taskName, capability);
        if (rawQuotes == null || rawQuotes.isEmpty()) {
            return summary;
        }

        // Filter valid quotes
        List<AgentQuoteResponse> validQuotes = rawQuotes.stream()
                .filter(q -> Boolean.TRUE.equals(q.getValid()))
                .collect(Collectors.toList());

        if (validQuotes.isEmpty()) {
            summary.setQuotes(rawQuotes);
            return summary;
        }

        // Calculate scores
        for (AgentQuoteResponse quote : validQuotes) {
            double score = scoringEngine.calculateScore(quote, validQuotes, customWeights);
            quote.setScore(score);
        }

        // Sort descending by score
        validQuotes.sort(Comparator.comparingDouble(AgentQuoteResponse::getScore).reversed());
        summary.setQuotes(validQuotes);

        // Find highlights
        summary.setFastestQuote(validQuotes.stream()
                .min(Comparator.comparingInt(AgentQuoteResponse::getEstimatedDuration)).orElse(null));

        summary.setCheapestQuote(validQuotes.stream()
                .min(Comparator.comparingDouble(AgentQuoteResponse::getQuotedPrice)).orElse(null));

        summary.setHighestReputationQuote(validQuotes.stream()
                .max(Comparator.comparingDouble(AgentQuoteResponse::getReputation)).orElse(null));

        summary.setHighestConfidenceQuote(validQuotes.stream()
                .max(Comparator.comparingDouble(AgentQuoteResponse::getConfidence)).orElse(null));

        summary.setLowestLoadQuote(validQuotes.stream()
                .min(Comparator.comparingDouble(AgentQuoteResponse::getCurrentLoad)).orElse(null));

        summary.setHighestOverallScoreQuote(validQuotes.get(0));

        return summary;
    }

    public List<TaskQuoteSummary> aggregateWorkflowQuotes(List<PlannedTaskDto> taskList, Map<String, List<AgentQuoteResponse>> taskQuotesMap, ScoringEngine.Weights customWeights) {
        List<TaskQuoteSummary> result = new ArrayList<>();
        if (taskList == null) return result;

        for (PlannedTaskDto task : taskList) {
            List<AgentQuoteResponse> quotes = taskQuotesMap.getOrDefault(task.getTaskId(), Collections.emptyList());
            TaskQuoteSummary summary = aggregateForTask(task, quotes, customWeights);
            result.add(summary);
        }
        return result;
    }
}
