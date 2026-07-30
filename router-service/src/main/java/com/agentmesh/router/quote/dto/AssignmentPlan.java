package com.agentmesh.router.quote.dto;

import java.util.ArrayList;
import java.util.List;

public class AssignmentPlan {

    private String workflowId;
    private List<TaskAssignment> assignments = new ArrayList<>();
    private Double totalQuotedPrice = 0.0;
    private Integer totalEstimatedDuration = 0;
    private String selectionStrategyUsed;
    private Long createdAt = System.currentTimeMillis();

    public AssignmentPlan() {}

    public AssignmentPlan(String workflowId, String selectionStrategyUsed) {
        this.workflowId = workflowId;
        this.selectionStrategyUsed = selectionStrategyUsed;
    }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public List<TaskAssignment> getAssignments() { return assignments; }
    public void setAssignments(List<TaskAssignment> assignments) {
        this.assignments = assignments;
        recalculateTotals();
    }

    public void addAssignment(TaskAssignment assignment) {
        this.assignments.add(assignment);
        recalculateTotals();
    }

    public Double getTotalQuotedPrice() { return totalQuotedPrice; }
    public void setTotalQuotedPrice(Double totalQuotedPrice) { this.totalQuotedPrice = totalQuotedPrice; }

    public Integer getTotalEstimatedDuration() { return totalEstimatedDuration; }
    public void setTotalEstimatedDuration(Integer totalEstimatedDuration) { this.totalEstimatedDuration = totalEstimatedDuration; }

    public String getSelectionStrategyUsed() { return selectionStrategyUsed; }
    public void setSelectionStrategyUsed(String selectionStrategyUsed) { this.selectionStrategyUsed = selectionStrategyUsed; }

    public Long getCreatedAt() { return createdAt; }
    public void setCreatedAt(Long createdAt) { this.createdAt = createdAt; }

    private void recalculateTotals() {
        if (assignments == null) return;
        this.totalQuotedPrice = assignments.stream()
                .filter(a -> a.getQuotedPrice() != null)
                .mapToDouble(TaskAssignment::getQuotedPrice)
                .sum();
        this.totalEstimatedDuration = assignments.stream()
                .filter(a -> a.getEstimatedDuration() != null)
                .mapToInt(TaskAssignment::getEstimatedDuration)
                .sum();
    }
}
