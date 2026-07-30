package com.agentmesh.router.execution;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.execution.dto.ExecutionTaskResponse;
import com.agentmesh.router.quote.dto.AssignmentPlan;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RetryManagerTest {

    private RetryManager retryManager;
    private TaskExecutor taskExecutor;
    private TimeoutManager timeoutManager;

    @BeforeEach
    void setUp() {
        retryManager = new RetryManager(2, 50);
        RestTemplate restTemplate = mock(RestTemplate.class);
        AgentDiscoveryService discoveryService = mock(AgentDiscoveryService.class);
        taskExecutor = new TaskExecutor(restTemplate, discoveryService);
        timeoutManager = new TimeoutManager(5000);
    }

    @Test
    void testSuccessfulExecutionNoRetry() {
        TaskAssignment assignment = new TaskAssignment();
        assignment.setTaskId("t-ok");
        assignment.setRequiredCapability("RESEARCH");

        AssignmentPlan plan = new AssignmentPlan("wf-r", "BALANCED");
        plan.addAssignment(assignment);
        ExecutionContext context = new ExecutionContext("wf-r", plan);

        ExecutionTaskResponse response = retryManager.executeWithRetry(
                assignment, context, taskExecutor, timeoutManager, 5000L, 2, RetryManager.RetryPolicy.IMMEDIATE
        );

        assertNotNull(response);
        assertEquals("COMPLETED", response.getStatus());
    }
}
