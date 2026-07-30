package com.agentmesh.router.execution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionStateMachineTest {

    private ExecutionStateMachine stateMachine;

    @BeforeEach
    void setUp() {
        stateMachine = new ExecutionStateMachine();
    }

    @Test
    void testValidWorkflowTransitions() {
        assertTrue(stateMachine.isValidWorkflowTransition(ExecutionStateMachine.WorkflowState.CREATED, ExecutionStateMachine.WorkflowState.QUEUED));
        assertTrue(stateMachine.isValidWorkflowTransition(ExecutionStateMachine.WorkflowState.QUEUED, ExecutionStateMachine.WorkflowState.PLANNING_COMPLETE));
        assertTrue(stateMachine.isValidWorkflowTransition(ExecutionStateMachine.WorkflowState.PLANNING_COMPLETE, ExecutionStateMachine.WorkflowState.ASSIGNMENT_COMPLETE));
        assertTrue(stateMachine.isValidWorkflowTransition(ExecutionStateMachine.WorkflowState.ASSIGNMENT_COMPLETE, ExecutionStateMachine.WorkflowState.EXECUTION_STARTED));
        assertTrue(stateMachine.isValidWorkflowTransition(ExecutionStateMachine.WorkflowState.EXECUTION_STARTED, ExecutionStateMachine.WorkflowState.RUNNING));
        assertTrue(stateMachine.isValidWorkflowTransition(ExecutionStateMachine.WorkflowState.RUNNING, ExecutionStateMachine.WorkflowState.COMPLETED));
    }

    @Test
    void testInvalidWorkflowTransitions() {
        assertFalse(stateMachine.isValidWorkflowTransition(ExecutionStateMachine.WorkflowState.COMPLETED, ExecutionStateMachine.WorkflowState.RUNNING));
        assertFalse(stateMachine.isValidWorkflowTransition(ExecutionStateMachine.WorkflowState.FAILED, ExecutionStateMachine.WorkflowState.RUNNING));
    }

    @Test
    void testValidTaskTransitions() {
        assertTrue(stateMachine.isValidTaskTransition(ExecutionStateMachine.TaskState.PENDING, ExecutionStateMachine.TaskState.READY));
        assertTrue(stateMachine.isValidTaskTransition(ExecutionStateMachine.TaskState.READY, ExecutionStateMachine.TaskState.RUNNING));
        assertTrue(stateMachine.isValidTaskTransition(ExecutionStateMachine.TaskState.RUNNING, ExecutionStateMachine.TaskState.FAILED));
        assertTrue(stateMachine.isValidTaskTransition(ExecutionStateMachine.TaskState.FAILED, ExecutionStateMachine.TaskState.RETRYING));
        assertTrue(stateMachine.isValidTaskTransition(ExecutionStateMachine.TaskState.FAILED, ExecutionStateMachine.TaskState.FALLBACK));
        assertTrue(stateMachine.isValidTaskTransition(ExecutionStateMachine.TaskState.FALLBACK, ExecutionStateMachine.TaskState.COMPLETED));
    }
}
