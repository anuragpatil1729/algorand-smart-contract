package com.agentmesh.router.execution;

import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DependencySchedulerTest {

    private DependencyScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new DependencyScheduler();
    }

    @Test
    void testGetReadyTasksWithNoDependencies() {
        AssignmentPlan plan = new AssignmentPlan("wf-1", "BALANCED");
        TaskAssignment a1 = new TaskAssignment();
        a1.setTaskId("t1");
        TaskAssignment a2 = new TaskAssignment();
        a2.setTaskId("t2");

        plan.addAssignment(a1);
        plan.addAssignment(a2);

        ExecutionContext context = new ExecutionContext("wf-1", plan);
        List<TaskAssignment> ready = scheduler.getReadyTasks(context, Map.of());

        assertEquals(2, ready.size());
    }

    @Test
    void testGetReadyTasksWithChainedDependencies() {
        AssignmentPlan plan = new AssignmentPlan("wf-2", "BALANCED");
        TaskAssignment a1 = new TaskAssignment();
        a1.setTaskId("t1");
        TaskAssignment a2 = new TaskAssignment();
        a2.setTaskId("t2");

        plan.addAssignment(a1);
        plan.addAssignment(a2);

        ExecutionContext context = new ExecutionContext("wf-2", plan);
        Map<String, List<String>> depsMap = Map.of("t2", List.of("t1"));

        // Step 1: Only t1 should be ready
        List<TaskAssignment> ready1 = scheduler.getReadyTasks(context, depsMap);
        assertEquals(1, ready1.size());
        assertEquals("t1", ready1.get(0).getTaskId());

        // Mark t1 completed
        context.updateTaskState("t1", ExecutionStateMachine.TaskState.COMPLETED);

        // Step 2: Now t2 should be ready
        List<TaskAssignment> ready2 = scheduler.getReadyTasks(context, depsMap);
        assertEquals(1, ready2.size());
        assertEquals("t2", ready2.get(0).getTaskId());
    }
}
