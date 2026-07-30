package com.agentmesh.router.quote.dto;

import java.util.ArrayList;
import java.util.List;

public class TaskQuoteSummary {

    private String taskId;
    private String taskName;
    private String requiredCapability;
    private List<AgentQuoteResponse> quotes = new ArrayList<>();
    private AgentQuoteResponse fastestQuote;
    private AgentQuoteResponse cheapestQuote;
    private AgentQuoteResponse highestReputationQuote;
    private AgentQuoteResponse highestConfidenceQuote;
    private AgentQuoteResponse lowestLoadQuote;
    private AgentQuoteResponse highestOverallScoreQuote;

    public TaskQuoteSummary() {}

    public TaskQuoteSummary(String taskId, String taskName, String requiredCapability) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.requiredCapability = requiredCapability;
    }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getRequiredCapability() { return requiredCapability; }
    public void setRequiredCapability(String requiredCapability) { this.requiredCapability = requiredCapability; }

    public List<AgentQuoteResponse> getQuotes() { return quotes; }
    public void setQuotes(List<AgentQuoteResponse> quotes) { this.quotes = quotes; }

    public AgentQuoteResponse getFastestQuote() { return fastestQuote; }
    public void setFastestQuote(AgentQuoteResponse fastestQuote) { this.fastestQuote = fastestQuote; }

    public AgentQuoteResponse getCheapestQuote() { return cheapestQuote; }
    public void setCheapestQuote(AgentQuoteResponse cheapestQuote) { this.cheapestQuote = cheapestQuote; }

    public AgentQuoteResponse getHighestReputationQuote() { return highestReputationQuote; }
    public void setHighestReputationQuote(AgentQuoteResponse highestReputationQuote) { this.highestReputationQuote = highestReputationQuote; }

    public AgentQuoteResponse getHighestConfidenceQuote() { return highestConfidenceQuote; }
    public void setHighestConfidenceQuote(AgentQuoteResponse highestConfidenceQuote) { this.highestConfidenceQuote = highestConfidenceQuote; }

    public AgentQuoteResponse getLowestLoadQuote() { return lowestLoadQuote; }
    public void setLowestLoadQuote(AgentQuoteResponse lowestLoadQuote) { this.lowestLoadQuote = lowestLoadQuote; }

    public AgentQuoteResponse getHighestOverallScoreQuote() { return highestOverallScoreQuote; }
    public void setHighestOverallScoreQuote(AgentQuoteResponse highestOverallScoreQuote) { this.highestOverallScoreQuote = highestOverallScoreQuote; }
}
