package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class WorkflowSerializer {

    private final ObjectMapper objectMapper;

    public WorkflowSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String serialize(WorkflowPlanResponseDto plan) {
        try {
            return objectMapper.writeValueAsString(plan);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize WorkflowPlanResponseDto", e);
        }
    }

    public WorkflowPlanResponseDto deserialize(String json) {
        try {
            return objectMapper.readValue(json, WorkflowPlanResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize WorkflowPlanResponseDto", e);
        }
    }
}
