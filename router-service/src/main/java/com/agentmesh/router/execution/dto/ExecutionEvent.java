package com.agentmesh.router.execution.dto;

import java.util.Map;
import java.util.UUID;

public class ExecutionEvent {

    private String eventId;
    private String eventType;
    private String workflowId;
    private String taskId;
    private String agentId;
    private Integer stage;
    private String message;
    private Long timestamp;
    private Map<String, Object> metadata;

    public ExecutionEvent() {
        this.eventId = "evt-" + UUID.randomUUID().toString().substring(0, 8);
        this.timestamp = System.currentTimeMillis();
    }

    public ExecutionEvent(String eventType, String workflowId, String taskId, String agentId, String message) {
        this();
        this.eventType = eventType;
        this.workflowId = workflowId;
        this.taskId = taskId;
        this.agentId = agentId;
        this.message = message;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }

    public Integer getStage() { return stage; }
    public void setStage(Integer stage) { this.stage = stage; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
