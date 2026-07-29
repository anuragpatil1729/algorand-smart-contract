package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.WorkflowPlanRequestDto;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlannerEngineTest {

    private PlannerEngine plannerEngine;

    @BeforeEach
    void setUp() {
        CapabilityResolver capabilityResolver = new CapabilityResolver();
        TaskComplexityEstimator estimator = new TaskComplexityEstimator();
        PlanningRulesEngine rulesEngine = new PlanningRulesEngine(capabilityResolver, estimator);
        TaskDependencyResolver dependencyResolver = new TaskDependencyResolver();
        TaskValidator validator = new TaskValidator(dependencyResolver);
        WorkflowOptimizer optimizer = new WorkflowOptimizer(dependencyResolver);
        WorkflowGraphBuilder graphBuilder = new WorkflowGraphBuilder();

        plannerEngine = new PlannerEngine(rulesEngine, validator, optimizer, dependencyResolver, graphBuilder);
    }

    @Test
    void testCreatePlanForStartupLandingPage() {
        WorkflowPlanRequestDto req = new WorkflowPlanRequestDto("Build a startup landing page with authentication, database and deployment.");

        WorkflowPlanResponseDto plan = plannerEngine.createPlan(req);

        assertNotNull(plan);
        assertNotNull(plan.getWorkflowId());
        assertTrue(plan.getTaskList().size() >= 5);
        assertNotNull(plan.getExecutionStages());
        assertTrue(plan.getExecutionStages().size() >= 3);
        assertNotNull(plan.getTotalEstimatedDurationSeconds());
        assertTrue(plan.getTotalEstimatedDurationSeconds() > 0);
        assertNotNull(plan.getGraphRepresentation());

        // Ensure capabilities are mapped, NOT agents
        assertTrue(plan.getRequiredCapabilities().contains("RESEARCH"));
        assertTrue(plan.getRequiredCapabilities().contains("FRONTEND_DEVELOPMENT"));
        assertTrue(plan.getRequiredCapabilities().contains("BACKEND_DEVELOPMENT"));
        assertTrue(plan.getRequiredCapabilities().contains("DATABASE_DESIGN"));
        assertTrue(plan.getRequiredCapabilities().contains("AUTHENTICATION"));
        assertTrue(plan.getRequiredCapabilities().contains("DEPLOYMENT"));

        // Retrieve saved plan
        WorkflowPlanResponseDto retrieved = plannerEngine.getPlan(plan.getWorkflowId());
        assertEquals(plan.getWorkflowId(), retrieved.getWorkflowId());
    }
}
