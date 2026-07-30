package com.agentmesh.router.execution;

import com.agentmesh.router.quote.dto.TaskAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ExecutionValidator {

    private static final Logger log = LoggerFactory.getLogger(ExecutionValidator.class);

    public Map<String, Object> validateWorkflowCompletion(ExecutionContext context) {
        Map<String, Object> report = new LinkedHashMap<>();
        if (context == null || context.getAssignmentPlan() == null) {
            report.put("valid", false);
            report.put("error", "Null context or assignment plan provided");
            return report;
        }

        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        Set<String> completedTasks = new HashSet<>(context.getCompletedTasks());
        Set<String> failedTasks = new HashSet<>(context.getFailedTasks());

        int totalTasks = context.getAssignmentPlan().getAssignments().size();
        int completedCount = completedTasks.size();

        // 1. Mandatory tasks completed check
        if (completedCount < totalTasks) {
            errors.add(String.format("Incomplete workflow: %d of %d tasks completed.", completedCount, totalTasks));
        }

        // 2. Unresolved failures check
        if (!failedTasks.isEmpty()) {
            errors.add(String.format("Unresolved task failures detected on tasks: %s", failedTasks));
        }

        // 3. Outputs existence check
        for (TaskAssignment assignment : context.getAssignmentPlan().getAssignments()) {
            String taskId = assignment.getTaskId();
            Object output = context.getTaskOutputs().get(taskId);
            if (output == null) {
                if (completedTasks.contains(taskId)) {
                    warnings.add("Task '" + taskId + "' completed but returned null output payload.");
                } else {
                    errors.add("Missing output for uncompleted task: '" + taskId + "'");
                }
            }
        }

        boolean isValid = errors.isEmpty();
        report.put("valid", isValid);
        report.put("totalTasks", totalTasks);
        report.put("completedTasks", completedCount);
        report.put("failedTasks", failedTasks.size());
        report.put("errors", errors);
        report.put("warnings", warnings);

        if (isValid) {
            log.info("Workflow '{}' execution validation passed successfully.", context.getWorkflowId());
        } else {
            log.warn("Workflow '{}' execution validation failed with {} errors.", context.getWorkflowId(), errors.size());
        }

        return report;
    }
}
