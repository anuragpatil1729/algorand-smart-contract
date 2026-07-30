package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionTaskResponse;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimeoutManagerTest {

    private TimeoutManager timeoutManager;

    @BeforeEach
    void setUp() {
        timeoutManager = new TimeoutManager(100);
    }

    @Test
    void testTimeoutExceededReturnsTimeoutStatus() {
        TaskAssignment assignment = new TaskAssignment();
        assignment.setTaskId("t-slow");
        assignment.setSelectedAgentId("slow-agent");

        AssignmentPlan plan = new AssignmentPlan("wf-t", "BALANCED");
        plan.addAssignment(assignment);
        ExecutionContext context = new ExecutionContext("wf-t", plan);

        TaskExecutor slowExecutor = mock(TaskExecutor.class);
        when(slowExecutor.executeTask(any(), any())).thenAnswer(invocation -> {
            Thread.sleep(1000);
            return new ExecutionTaskResponse("e1", "t-slow", "COMPLETED", "Done", 1000L, null);
        });

        ExecutionTaskResponse response = timeoutManager.executeWithTimeout(assignment, context, slowExecutor, 100L);

        assertNotNull(response);
        assertEquals("TIMEOUT", response.getStatus());
        assertTrue(response.getError().contains("timed out"));
    }
}
