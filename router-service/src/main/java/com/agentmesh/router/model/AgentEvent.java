package com.agentmesh.router.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agent_events")
public class AgentEvent {

    @Id
    private String id;

    @Column(name = "agent_id", nullable = false)
    private String agentId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(length = 2000)
    private String message;

    private String level = "INFO";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public AgentEvent() {}

    public AgentEvent(String id, String agentId, String eventType, String message, String level, LocalDateTime createdAt) {
        this.id = id;
        this.agentId = agentId;
        this.eventType = eventType;
        this.message = message;
        this.level = level != null ? level : "INFO";
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
