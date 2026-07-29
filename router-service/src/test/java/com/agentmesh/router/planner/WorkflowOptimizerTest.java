package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowOptimizerTest {

    private final TaskDependencyResolver dependencyResolver = new TaskDependencyResolver();
    private final WorkflowOptimizer optimizer = new WorkflowOptimizer(dependencyResolver);

    @Test
    void testParallelExecutionStagesOptimization() {
        PlannedTaskDto r = new PlannedTaskDto();
        r.setTaskId("t-research");
        r.setDependencies(new ArrayList<>());

        PlannedTaskDto logo = new PlannedTaskDto();
        logo.setTaskId("t-logo");
        logo.setDependencies(List.of("t-research"));

        PlannedTaskDto be = new PlannedTaskDto();
        be.setTaskId("t-backend");
        be.setDependencies(List.of("t-research"));

        PlannedTaskDto fe = new PlannedTaskDto();
        fe.setTaskId("t-frontend");
        fe.setDependencies(List.of("t-research", "t-logo"));

        Map<Integer, List<PlannedTaskDto>> stages = optimizer.optimizeExecutionStages(List.of(r, logo, be, fe));

        assertEquals(3, stages.size());
        assertEquals(1, stages.get(1).size()); // Stage 1: Research
        assertEquals(2, stages.get(2).size()); // Stage 2: Logo, Backend (Parallel!)
        assertEquals(1, stages.get(3).size()); // Stage 3: Frontend

        List<List<String>> groups = optimizer.extractParallelGroups(stages);
        assertEquals(3, groups.size());
        assertTrue(groups.get(1).contains("t-logo") && groups.get(1).contains("t-backend"));
    }
}
