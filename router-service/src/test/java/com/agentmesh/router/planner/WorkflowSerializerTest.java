package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowSerializerTest {

    private final WorkflowSerializer serializer = new WorkflowSerializer();

    @Test
    void testSerializationAndDeserialization() {
        WorkflowPlanResponseDto plan = new WorkflowPlanResponseDto();
        plan.setWorkflowId("wf-test-123");
        plan.setPrompt("Build landing page");

        PlannedTaskDto task = new PlannedTaskDto();
        task.setTaskId("t1");
        task.setTaskName("Research");
        task.setRequiredCapability("RESEARCH");
        plan.setTaskList(List.of(task));

        String json = serializer.serialize(plan);
        assertNotNull(json);
        assertTrue(json.contains("wf-test-123"));

        WorkflowPlanResponseDto restored = serializer.deserialize(json);
        assertNotNull(restored);
        assertEquals("wf-test-123", restored.getWorkflowId());
        assertEquals("Build landing page", restored.getPrompt());
        assertEquals(1, restored.getTaskList().size());
    }
}
