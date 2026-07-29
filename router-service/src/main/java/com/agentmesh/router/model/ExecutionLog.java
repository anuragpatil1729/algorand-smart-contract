package com.agentmesh.router.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "execution_logs")
public class ExecutionLog {

    @Id
    private String id;

    @Column(name = "workflow_id", nullable = false)
    private String workflowId;

    @Column(name = "task_id")
    private String taskId;

    @Column(name = "agent_id")
    private String agentId;

    @Column(name = "log_level")
    private String logLevel = "INFO";

    @Column(nullable = false, length = 2000)
    private String message;

    private LocalDateTime timestamp;

    public ExecutionLog() {}

    public ExecutionLog(String id, String workflowId, String taskId, String agentId, String logLevel, String message, LocalDateTime timestamp) {
        this.id = id;
        this.workflowId = workflowId;
        this.taskId = taskId;
        this.agentId = agentId;
        this.logLevel = logLevel != null ? logLevel : "INFO";
        this.message = message;
        this.timestamp = timestamp;
    }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String workflowId;
        private String taskId;
        private String agentId;
        private String logLevel = "INFO";
        private String message;
        private LocalDateTime timestamp;

        public Builder id(String id) { this.id = id; return this; }
        public Builder workflowId(String workflowId) { this.workflowId = workflowId; return this; }
        public Builder taskId(String taskId) { this.taskId = taskId; return this; }
        public Builder agentId(String agentId) { this.agentId = agentId; return this; }
        public Builder logLevel(String logLevel) { this.logLevel = logLevel; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public ExecutionLog build() {
            return new ExecutionLog(id, workflowId, taskId, agentId, logLevel, message, timestamp);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }
    public String getLogLevel() { return logLevel; }
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
