package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanningRulesEngineTest {

    private final CapabilityResolver capabilityResolver = new CapabilityResolver();
    private final TaskComplexityEstimator estimator = new TaskComplexityEstimator();
    private final PlanningRulesEngine rulesEngine = new PlanningRulesEngine(capabilityResolver, estimator);

    @Test
    void testPromptDecomposition() {
        String prompt = "Build a startup landing page with authentication, database and deployment.";
        List<PlannedTaskDto> tasks = rulesEngine.generateTasks(prompt);

        assertFalse(tasks.isEmpty(), "Generated tasks should not be empty");
        for (PlannedTaskDto t : tasks) {
            System.out.println("Generated Task: " + t.getTaskId() + " | Cap: " + t.getRequiredCapability() + " | Name: " + t.getTaskName());
        }

        assertTrue(tasks.stream().anyMatch(t -> "research".equals(t.getRequiredCapability())), "Should have research");
        assertTrue(tasks.stream().anyMatch(t -> "code-generation".equals(t.getRequiredCapability())), "Should have code-generation");
        assertTrue(tasks.stream().anyMatch(t -> "database".equals(t.getRequiredCapability())), "Should have database");
        assertTrue(tasks.stream().anyMatch(t -> "testing".equals(t.getRequiredCapability())), "Should have testing");
        assertTrue(tasks.stream().anyMatch(t -> "deployment".equals(t.getRequiredCapability())), "Should have deployment");
    }
}
