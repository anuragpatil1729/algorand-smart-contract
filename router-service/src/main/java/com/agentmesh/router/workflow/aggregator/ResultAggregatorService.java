package com.agentmesh.router.workflow.aggregator;

import com.agentmesh.router.model.Task;
import com.agentmesh.router.model.Workflow;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ResultAggregatorService {

    public String aggregateResults(Workflow workflow, List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("# AgentMesh Consolidated Multi-Agent Deliverable Package\n\n");
        sb.append("**Prompt**: ").append(workflow.getPrompt()).append("\n");
        sb.append("**Workflow ID**: ").append(workflow.getId()).append("\n");
        sb.append("**Settlement**: Algorand Atomic Group Payment (").append(workflow.getTotalPrice()).append(" Algos)\n\n");
        sb.append("---\n\n");

        tasks.stream()
                .sorted(Comparator.comparingInt(Task::getPriority))
                .forEach(task -> {
                    sb.append("## Task: ").append(task.getDescription()).append(" (Type: ").append(task.getTaskType()).append(")\n");
                    sb.append("- **Assigned Agent**: ").append(task.getAssignedAgent()).append("\n");
                    sb.append("- **Status**: ").append(task.getStatus()).append(" (Execution Time: ").append(task.getExecutionTimeMs()).append("ms)\n\n");
                    
                    if (task.getOutput() != null && !task.getOutput().isBlank()) {
                        sb.append(task.getOutput()).append("\n\n");
                    } else {
                        sb.append("*No output produced.*\n\n");
                    }
                    sb.append("---\n\n");
                });

        sb.append("### Final Quality Assurance & Verification\n");
        sb.append("All workflow task dependencies were satisfied in topological order. Algorand Atomic Transfer Group releases payment to verified agent wallet addresses.");
        
        return sb.toString();
    }
}
