package com.agentmesh.router.quote.dto;

import java.util.ArrayList;
import java.util.List;

public class TaskAssignment {

    private String taskId;
    private String taskName;
    private String requiredCapability;
    private String selectedAgentId;
    private String selectedAgentName;
    private String selectedAgentEndpoint;
    private Double quotedPrice;
    private Integer estimatedDuration;
    private String selectionReason;
    private List<AgentQuoteResponse> alternativeAgents = new ArrayList<>();

    public TaskAssignment() {}

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getRequiredCapability() { return requiredCapability; }
    public void setRequiredCapability(String requiredCapability) { this.requiredCapability = requiredCapability; }

    public String getSelectedAgentId() { return selectedAgentId; }
    public void setSelectedAgentId(String selectedAgentId) { this.selectedAgentId = selectedAgentId; }

    public String getSelectedAgentName() { return selectedAgentName; }
    public void setSelectedAgentName(String selectedAgentName) { this.selectedAgentName = selectedAgentName; }

    public String getSelectedAgentEndpoint() { return selectedAgentEndpoint; }
    public void setSelectedAgentEndpoint(String selectedAgentEndpoint) { this.selectedAgentEndpoint = selectedAgentEndpoint; }

    public Double getQuotedPrice() { return quotedPrice; }
    public Double getPrice() { return quotedPrice != null ? quotedPrice : 0.0; }
    public void setQuotedPrice(Double quotedPrice) { this.quotedPrice = quotedPrice; }

    public Integer getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(Integer estimatedDuration) { this.estimatedDuration = estimatedDuration; }

    public String getSelectionReason() { return selectionReason; }
    public void setSelectionReason(String selectionReason) { this.selectionReason = selectionReason; }

    public List<AgentQuoteResponse> getAlternativeAgents() { return alternativeAgents; }
    public void setAlternativeAgents(List<AgentQuoteResponse> alternativeAgents) { this.alternativeAgents = alternativeAgents; }
}
