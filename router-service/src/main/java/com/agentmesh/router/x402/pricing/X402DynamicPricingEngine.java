package com.agentmesh.router.x402.pricing;

import com.agentmesh.router.execution.dto.WorkflowExecutionRequest;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class X402DynamicPricingEngine {

    private final double baseWorkflowFeeUsdc;
    private final double perTaskFeeUsdc;
    private final double perComputeSecondFeeUsdc;

    public X402DynamicPricingEngine(
            @Value("${agentmesh.x402.pricing.base-fee-usdc:1.00}") double baseWorkflowFeeUsdc,
            @Value("${agentmesh.x402.pricing.per-task-fee-usdc:0.50}") double perTaskFeeUsdc,
            @Value("${agentmesh.x402.pricing.per-second-fee-usdc:0.05}") double perComputeSecondFeeUsdc
    ) {
        this.baseWorkflowFeeUsdc = baseWorkflowFeeUsdc;
        this.perTaskFeeUsdc = perTaskFeeUsdc;
        this.perComputeSecondFeeUsdc = perComputeSecondFeeUsdc;
    }

    public double calculatePayPerUsePrice(WorkflowExecutionRequest request) {
        if (request == null) return baseWorkflowFeeUsdc;

        AssignmentPlan plan = request.getAssignmentPlan();
        if (plan == null || plan.getAssignments() == null || plan.getAssignments().isEmpty()) {
            return baseWorkflowFeeUsdc;
        }

        double totalQuotedCost = plan.getTotalQuotedPrice() != null && plan.getTotalQuotedPrice() > 0 ?
                plan.getTotalQuotedPrice() : 0.0;
        int taskCount = plan.getAssignments().size();
        int estimatedDuration = plan.getTotalEstimatedDuration() != null ? plan.getTotalEstimatedDuration() : (taskCount * 10);

        double dynamicPrice = baseWorkflowFeeUsdc + (taskCount * perTaskFeeUsdc) + (estimatedDuration * perComputeSecondFeeUsdc);

        // Include agent quote costs if available
        if (totalQuotedCost > 0.0) {
            dynamicPrice += (totalQuotedCost * 0.10); // 10% platform margin
        }

        return Math.round(dynamicPrice * 100.0) / 100.0;
    }

    public double calculatePriceForWorkflow(String workflowId, int taskCount, int estimatedSeconds) {
        double price = baseWorkflowFeeUsdc + (taskCount * perTaskFeeUsdc) + (estimatedSeconds * perComputeSecondFeeUsdc);
        return Math.round(price * 100.0) / 100.0;
    }
}
