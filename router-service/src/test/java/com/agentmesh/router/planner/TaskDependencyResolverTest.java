package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskDependencyResolverTest {

    private final TaskDependencyResolver resolver = new TaskDependencyResolver();

    @Test
    void testTopologicalSorting() {
        PlannedTaskDto t1 = new PlannedTaskDto();
        t1.setTaskId("t1");
        t1.setEstimatedDurationSeconds(10);
        t1.setDependencies(new ArrayList<>());

        PlannedTaskDto t2 = new PlannedTaskDto();
        t2.setTaskId("t2");
        t2.setEstimatedDurationSeconds(15);
        t2.setDependencies(List.of("t1"));

        PlannedTaskDto t3 = new PlannedTaskDto();
        t3.setTaskId("t3");
        t3.setEstimatedDurationSeconds(20);
        t3.setDependencies(List.of("t2"));

        List<PlannedTaskDto> sorted = resolver.resolveTopologicalOrder(List.of(t3, t1, t2));

        assertEquals("t1", sorted.get(0).getTaskId());
        assertEquals("t2", sorted.get(1).getTaskId());
        assertEquals("t3", sorted.get(2).getTaskId());

        int duration = resolver.calculateCriticalPathDurationSeconds(sorted);
        assertEquals(45, duration); // 10 + 15 + 20
    }

    @Test
    void testCycleDetection() {
        PlannedTaskDto t1 = new PlannedTaskDto();
        t1.setTaskId("t1");
        t1.setDependencies(List.of("t2"));

        PlannedTaskDto t2 = new PlannedTaskDto();
        t2.setTaskId("t2");
        t2.setDependencies(List.of("t1"));

        assertTrue(resolver.hasCycle(List.of(t1, t2)), "Resolver should detect cycle in circular graph");
    }
}
