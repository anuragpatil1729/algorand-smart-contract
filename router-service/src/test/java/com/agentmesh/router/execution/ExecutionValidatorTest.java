package com.agentmesh.router.execution;

import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionValidatorTest {

    private ExecutionValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ExecutionValidator();
    }

    @Test
    void testValidateSuccessfulWorkflow() {
        AssignmentPlan plan = new AssignmentPlan("wf-1", "BALANCED");
        TaskAssignment a1 = new TaskAssignment();
        a1.setTaskId("t1");
        a1.setTaskName("Research");

        plan.addAssignment(a1);

        ExecutionContext context = new ExecutionContext("wf-1", plan);
        context.updateTaskState("t1", ExecutionStateMachine.TaskState.COMPLETED);
        context.setTaskOutput("t1", "Market research results");

        Map<String, Object> report = validator.validateWorkflowCompletion(context);

        assertTrue((Boolean) report.get("valid"));
        assertEquals(1, report.get("completedTasks"));
    }

    @Test
    void testValidateUnresolvedFailures() {
        AssignmentPlan plan = new AssignmentPlan("wf-2", "BALANCED");
        TaskAssignment a1 = new TaskAssignment();
        a1.setTaskId("t1");

        plan.addAssignment(a1);

        ExecutionContext context = new ExecutionContext("wf-2", plan);
        context.updateTaskState("t1", ExecutionStateMachine.TaskState.FAILED_PERMANENTLY);

        Map<String, Object> report = validator.validateWorkflowCompletion(context);

        assertFalse((Boolean) report.get("valid"));
        assertEquals(1, report.get("failedTasks"));
    }
}
