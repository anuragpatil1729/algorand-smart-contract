package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TaskValidator {

    private final TaskDependencyResolver dependencyResolver;

    public TaskValidator(TaskDependencyResolver dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
    }

    public List<String> validateTasks(List<PlannedTaskDto> tasks) {
        List<String> errors = new ArrayList<>();
        if (tasks == null || tasks.isEmpty()) {
            errors.add("Workflow task list cannot be empty");
            return errors;
        }

        Set<String> taskIds = new HashSet<>();
        for (PlannedTaskDto t : tasks) {
            if (t.getTaskId() == null || t.getTaskId().isBlank()) {
                errors.add("Task found with missing or blank taskId");
            } else if (!taskIds.add(t.getTaskId())) {
                errors.add("Duplicate taskId detected: " + t.getTaskId());
            }

            if (t.getRequiredCapability() == null || t.getRequiredCapability().isBlank()) {
                errors.add("Task " + t.getTaskId() + " has missing required capability");
            }
        }

        for (PlannedTaskDto t : tasks) {
            if (t.getDependencies() != null) {
                for (String dep : t.getDependencies()) {
                    if (!taskIds.contains(dep)) {
                        errors.add("Task " + t.getTaskId() + " depends on non-existent dependency: " + dep);
                    }
                }
            }
        }

        if (dependencyResolver.hasCycle(tasks)) {
            errors.add("Circular dependency (cycle) detected in task graph");
        }

        return errors;
    }
}
