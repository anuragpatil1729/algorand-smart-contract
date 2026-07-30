package com.agentmesh.router.execution;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DependencyScheduler {

    private static final Logger log = LoggerFactory.getLogger(DependencyScheduler.class);

    public List<TaskAssignment> getReadyTasks(ExecutionContext context, Map<String, List<String>> taskDependenciesMap) {
        if (context == null || context.getAssignmentPlan() == null) {
            return Collections.emptyList();
        }

        List<TaskAssignment> readyTasks = new ArrayList<>();
        Map<String, ExecutionStateMachine.TaskState> states = context.getTaskStates();
        Set<String> completedTaskIds = new HashSet<>(context.getCompletedTasks());

        for (TaskAssignment assignment : context.getAssignmentPlan().getAssignments()) {
            String taskId = assignment.getTaskId();
            ExecutionStateMachine.TaskState state = states.getOrDefault(taskId, ExecutionStateMachine.TaskState.PENDING);

            if (state == ExecutionStateMachine.TaskState.PENDING || state == ExecutionStateMachine.TaskState.READY) {
                List<String> dependencies = taskDependenciesMap.getOrDefault(taskId, Collections.emptyList());
                if (areDependenciesMet(dependencies, completedTaskIds)) {
                    readyTasks.add(assignment);
                }
            }
        }

        return readyTasks;
    }

    public boolean areDependenciesMet(List<String> dependencies, Set<String> completedTaskIds) {
        if (dependencies == null || dependencies.isEmpty()) {
            return true;
        }
        for (String dep : dependencies) {
            if (dep != null && !dep.isBlank() && !completedTaskIds.contains(dep)) {
                return false;
            }
        }
        return true;
    }

    public List<List<TaskAssignment>> computeExecutionStages(AssignmentPlan plan, Map<String, List<String>> dependenciesMap) {
        List<List<TaskAssignment>> stages = new ArrayList<>();
        if (plan == null || plan.getAssignments() == null || plan.getAssignments().isEmpty()) {
            return stages;
        }

        Set<String> processed = new HashSet<>();
        List<TaskAssignment> remaining = new ArrayList<>(plan.getAssignments());

        while (!remaining.isEmpty()) {
            List<TaskAssignment> stage = new ArrayList<>();
            for (TaskAssignment assignment : remaining) {
                List<String> deps = dependenciesMap.getOrDefault(assignment.getTaskId(), Collections.emptyList());
                if (areDependenciesMet(deps, processed)) {
                    stage.add(assignment);
                }
            }

            if (stage.isEmpty()) {
                log.warn("Cyclic or unresolvable dependencies detected in assignment plan. Fallback scheduling remaining tasks.");
                stage.addAll(remaining);
            }

            stages.add(stage);
            for (TaskAssignment a : stage) {
                processed.add(a.getTaskId());
                remaining.remove(a);
            }
        }

        return stages;
    }
}
