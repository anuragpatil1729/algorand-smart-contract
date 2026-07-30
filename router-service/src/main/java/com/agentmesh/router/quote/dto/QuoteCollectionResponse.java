package com.agentmesh.router.quote.dto;

import java.util.ArrayList;
import java.util.List;

public class QuoteCollectionResponse {

    private String workflowId;
    private int totalTasksCount;
    private int totalQuotesCollected;
    private List<TaskQuoteSummary> taskSummaries = new ArrayList<>();

    public QuoteCollectionResponse() {}

    public QuoteCollectionResponse(String workflowId, int totalTasksCount, int totalQuotesCollected, List<TaskQuoteSummary> taskSummaries) {
        this.workflowId = workflowId;
        this.totalTasksCount = totalTasksCount;
        this.totalQuotesCollected = totalQuotesCollected;
        this.taskSummaries = taskSummaries;
    }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public int getTotalTasksCount() { return totalTasksCount; }
    public void setTotalTasksCount(int totalTasksCount) { this.totalTasksCount = totalTasksCount; }

    public int getTotalQuotesCollected() { return totalQuotesCollected; }
    public void setTotalQuotesCollected(int totalQuotesCollected) { this.totalQuotesCollected = totalQuotesCollected; }

    public List<TaskQuoteSummary> getTaskSummaries() { return taskSummaries; }
    public void setTaskSummaries(List<TaskQuoteSummary> taskSummaries) { this.taskSummaries = taskSummaries; }
}
