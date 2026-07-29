package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskValidatorTest {

    private final TaskDependencyResolver dependencyResolver = new TaskDependencyResolver();
    private final TaskValidator validator = new TaskValidator(dependencyResolver);

    @Test
    void testValidateValidTasks() {
        PlannedTaskDto t1 = new PlannedTaskDto();
        t1.setTaskId("t1");
        t1.setRequiredCapability("RESEARCH");
        t1.setDependencies(new ArrayList<>());

        PlannedTaskDto t2 = new PlannedTaskDto();
        t2.setTaskId("t2");
        t2.setRequiredCapability("TESTING");
        t2.setDependencies(List.of("t1"));

        List<String> errors = validator.validateTasks(List.of(t1, t2));
        assertTrue(errors.isEmpty(), "Valid DAG should have 0 validation errors");
    }

    @Test
    void testValidateInvalidTasksMissingDependencyAndDuplicateId() {
        PlannedTaskDto t1 = new PlannedTaskDto();
        t1.setTaskId("t1");
        t1.setRequiredCapability("RESEARCH");
        t1.setDependencies(List.of("non-existent-task"));

        PlannedTaskDto t2 = new PlannedTaskDto();
        t2.setTaskId("t1"); // duplicate ID!
        t2.setRequiredCapability("TESTING");

        List<String> errors = validator.validateTasks(List.of(t1, t2));
        assertFalse(errors.isEmpty(), "Validation should detect errors");
        assertTrue(errors.stream().anyMatch(e -> e.contains("Duplicate taskId")));
        assertTrue(errors.stream().anyMatch(e -> e.contains("non-existent dependency")));
    }
}
