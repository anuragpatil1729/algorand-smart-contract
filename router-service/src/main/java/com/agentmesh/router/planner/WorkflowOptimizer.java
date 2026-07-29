package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WorkflowOptimizer {

    private final TaskDependencyResolver dependencyResolver;

    public WorkflowOptimizer(TaskDependencyResolver dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
    }

    public Map<Integer, List<PlannedTaskDto>> optimizeExecutionStages(List<PlannedTaskDto> tasks) {
        if (tasks == null || tasks.isEmpty()) return Collections.emptyMap();

        List<PlannedTaskDto> sorted = dependencyResolver.resolveTopologicalOrder(tasks);
        Map<String, Integer> taskStageMap = new HashMap<>();
        Map<Integer, List<PlannedTaskDto>> stages = new TreeMap<>();

        for (PlannedTaskDto t : sorted) {
            int maxParentStage = 0;
            if (t.getDependencies() != null) {
                for (String dep : t.getDependencies()) {
                    maxParentStage = Math.max(maxParentStage, taskStageMap.getOrDefault(dep, 0));
                }
            }
            int currentStage = maxParentStage + 1;
            t.setExecutionStage(currentStage);
            taskStageMap.put(t.getTaskId(), currentStage);

            stages.computeIfAbsent(currentStage, k -> new ArrayList<>()).add(t);
        }

        return stages;
    }

    public List<List<String>> extractParallelGroups(Map<Integer, List<PlannedTaskDto>> stages) {
        List<List<String>> groups = new ArrayList<>();
        if (stages == null) return groups;

        for (Map.Entry<Integer, List<PlannedTaskDto>> entry : stages.entrySet()) {
            List<String> stageTaskIds = new ArrayList<>();
            for (PlannedTaskDto t : entry.getValue()) {
                stageTaskIds.add(t.getTaskId());
            }
            groups.add(stageTaskIds);
        }
        return groups;
    }
}
