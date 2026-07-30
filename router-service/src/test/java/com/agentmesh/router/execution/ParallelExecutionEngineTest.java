package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionTaskResponse;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParallelExecutionEngineTest {

    private ParallelExecutionEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ParallelExecutionEngine(5);
    }

    @Test
    void testExecuteTasksConcurrently() {
        TaskAssignment a1 = new TaskAssignment();
        a1.setTaskId("t1");
        TaskAssignment a2 = new TaskAssignment();
        a2.setTaskId("t2");

        AssignmentPlan plan = new AssignmentPlan("wf-p", "BALANCED");
        plan.addAssignment(a1);
        plan.addAssignment(a2);

        ExecutionContext context = new ExecutionContext("wf-p", plan);

        Map<String, ExecutionTaskResponse> results = engine.executeTasksConcurrently(
                List.of(a1, a2),
                context,
                assignment -> new ExecutionTaskResponse("exec-" + assignment.getTaskId(), assignment.getTaskId(), "COMPLETED", "Done", 50L, null)
        );

        assertEquals(2, results.size());
        assertTrue(results.containsKey("t1"));
        assertTrue(results.containsKey("t2"));
        assertEquals("COMPLETED", results.get("t1").getStatus());
    }
}
