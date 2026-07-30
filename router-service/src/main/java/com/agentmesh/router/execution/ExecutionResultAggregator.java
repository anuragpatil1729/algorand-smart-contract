package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionTaskResponse;
import com.agentmesh.router.execution.dto.WorkflowResult;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ExecutionResultAggregator {

    public WorkflowResult aggregateResults(ExecutionContext context, Map<String, Object> validationReport) {
        if (context == null) {
            return new WorkflowResult("unknown", "FAILED");
        }

        String workflowId = context.getWorkflowId();
        String status = context.getWorkflowState() != null ? context.getWorkflowState().name() : "COMPLETED";

        WorkflowResult result = new WorkflowResult(workflowId, status);
        result.setTaskOutputs(new HashMap<>(context.getTaskOutputs()));
        result.setValidationReport(validationReport);

        long totalTime = System.currentTimeMillis() - context.getStartTime();
        result.setTotalExecutionTimeMs(totalTime);

        List<ExecutionTaskResponse> responsesList = new ArrayList<>();

        if (context.getAssignmentPlan() != null && context.getAssignmentPlan().getAssignments() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("# AgentMesh Workflow Summary Report\n");
            sb.append("Workflow ID: ").append(workflowId).append("\n");
            sb.append("Status: ").append(status).append("\n");
            sb.append("Total Duration: ").append(totalTime).append(" ms\n\n");
            sb.append("## Executed Task Deliverables\n\n");

            for (TaskAssignment assignment : context.getAssignmentPlan().getAssignments()) {
                String taskId = assignment.getTaskId();
                ExecutionTaskResponse taskRes = context.getTaskResponses().get(taskId);
                if (taskRes != null) {
                    responsesList.add(taskRes);
                }

                Object output = context.getTaskOutputs().get(taskId);
                sb.append("### Task: ").append(assignment.getTaskName())
                        .append(" (ID: ").append(taskId).append(")\n");
                sb.append("- **Assigned Agent**: ").append(assignment.getSelectedAgentName()).append(" (").append(assignment.getSelectedAgentId()).append(")\n");
                sb.append("- **Capability**: ").append(assignment.getRequiredCapability()).append("\n");
                sb.append("- **Quoted Price**: $").append(assignment.getQuotedPrice()).append("\n");
                sb.append("- **Execution Output**:\n```\n");
                sb.append(output != null ? output.toString() : "No output recorded");
                sb.append("\n```\n\n");
            }

            result.setAggregatedOutput(sb.toString());
        }

        result.setTaskResults(responsesList);
        return result;
    }
}
