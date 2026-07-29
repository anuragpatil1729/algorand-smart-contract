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

        assertTrue(tasks.stream().anyMatch(t -> "RESEARCH".equals(t.getRequiredCapability())), "Should have RESEARCH");
        assertTrue(tasks.stream().anyMatch(t -> "FRONTEND_DEVELOPMENT".equals(t.getRequiredCapability())), "Should have FRONTEND_DEVELOPMENT");
        assertTrue(tasks.stream().anyMatch(t -> "BACKEND_DEVELOPMENT".equals(t.getRequiredCapability())), "Should have BACKEND_DEVELOPMENT");
        assertTrue(tasks.stream().anyMatch(t -> "DATABASE_DESIGN".equals(t.getRequiredCapability())), "Should have DATABASE_DESIGN");
        assertTrue(tasks.stream().anyMatch(t -> "AUTHENTICATION".equals(t.getRequiredCapability())), "Should have AUTHENTICATION");
        assertTrue(tasks.stream().anyMatch(t -> "TESTING".equals(t.getRequiredCapability())), "Should have TESTING");
        assertTrue(tasks.stream().anyMatch(t -> "DEPLOYMENT".equals(t.getRequiredCapability())), "Should have DEPLOYMENT");
    }
}
