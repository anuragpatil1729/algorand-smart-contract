package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TaskDependencyResolver {

    public List<PlannedTaskDto> resolveTopologicalOrder(List<PlannedTaskDto> tasks) {
        if (tasks == null || tasks.isEmpty()) return Collections.emptyList();

        Map<String, PlannedTaskDto> taskMap = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, List<String>> adjList = new HashMap<>();

        for (PlannedTaskDto t : tasks) {
            taskMap.put(t.getTaskId(), t);
            inDegree.put(t.getTaskId(), 0);
            adjList.put(t.getTaskId(), new ArrayList<>());
        }

        for (PlannedTaskDto t : tasks) {
            if (t.getDependencies() != null) {
                for (String dep : t.getDependencies()) {
                    if (taskMap.containsKey(dep)) {
                        adjList.get(dep).add(t.getTaskId());
                        inDegree.put(t.getTaskId(), inDegree.get(t.getTaskId()) + 1);
                    }
                }
            }
        }

        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<PlannedTaskDto> sorted = new ArrayList<>();
        while (!queue.isEmpty()) {
            String currId = queue.poll();
            PlannedTaskDto curr = taskMap.get(currId);
            sorted.add(curr);

            for (String neighbor : adjList.get(currId)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sorted.size() != tasks.size()) {
            throw new IllegalStateException("Circular dependency detected in workflow DAG!");
        }

        return sorted;
    }

    public boolean hasCycle(List<PlannedTaskDto> tasks) {
        try {
            resolveTopologicalOrder(tasks);
            return false;
        } catch (IllegalStateException e) {
            return true;
        }
    }

    public int calculateCriticalPathDurationSeconds(List<PlannedTaskDto> tasks) {
        if (tasks == null || tasks.isEmpty()) return 0;

        List<PlannedTaskDto> sorted = resolveTopologicalOrder(tasks);
        Map<String, Integer> maxDurationToTask = new HashMap<>();

        int totalMax = 0;
        for (PlannedTaskDto t : sorted) {
            int duration = t.getEstimatedDurationSeconds() != null ? t.getEstimatedDurationSeconds() : 10;
            int maxParentDuration = 0;

            if (t.getDependencies() != null) {
                for (String dep : t.getDependencies()) {
                    maxParentDuration = Math.max(maxParentDuration, maxDurationToTask.getOrDefault(dep, 0));
                }
            }

            int currTotal = maxParentDuration + duration;
            maxDurationToTask.put(t.getTaskId(), currTotal);
            totalMax = Math.max(totalMax, currTotal);
        }

        return totalMax;
    }
}
