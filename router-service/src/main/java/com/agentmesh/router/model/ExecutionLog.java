package com.agentmesh.router.model;

import com.agentmesh.router.model.enums.LogLevel;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "execution_logs")
public class ExecutionLog {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "agent_id")
    private String agentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_level", nullable = false)
    private LogLevel logLevel = LogLevel.INFO;

    @Column(nullable = false, length = 2000)
    private String message;

    private LocalDateTime timestamp;

    public ExecutionLog() {}

    public ExecutionLog(String id, Workflow workflow, Task task, String agentId, LogLevel logLevel, String message, LocalDateTime timestamp) {
        this.id = id;
        this.workflow = workflow;
        this.task = task;
        this.agentId = agentId;
        this.logLevel = logLevel != null ? logLevel : LogLevel.INFO;
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
        private Workflow workflow;
        private Task task;
        private String agentId;
        private LogLevel logLevel = LogLevel.INFO;
        private String message;
        private LocalDateTime timestamp;

        public Builder id(String id) { this.id = id; return this; }
        public Builder workflow(Workflow workflow) { this.workflow = workflow; return this; }
        public Builder workflowId(String workflowId) {
            if (workflowId != null) this.workflow = Workflow.builder().id(workflowId).build();
            return this;
        }

        public Builder task(Task task) { this.task = task; return this; }
        public Builder taskId(String taskId) {
            if (taskId != null) this.task = Task.builder().id(taskId).build();
            return this;
        }

        public Builder agentId(String agentId) { this.agentId = agentId; return this; }
        public Builder logLevel(LogLevel logLevel) { this.logLevel = logLevel; return this; }

        public Builder logLevel(String logLevelStr) {
            if (logLevelStr != null) {
                try {
                    this.logLevel = LogLevel.valueOf(logLevelStr.toUpperCase());
                } catch (Exception e) {
                    this.logLevel = LogLevel.INFO;
                }
            }
            return this;
        }

        public Builder message(String message) { this.message = message; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public ExecutionLog build() {
            return new ExecutionLog(id, workflow, task, agentId, logLevel, message, timestamp);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Workflow getWorkflow() { return workflow; }
    public void setWorkflow(Workflow workflow) { this.workflow = workflow; }

    public String getWorkflowId() { return workflow != null ? workflow.getId() : null; }
    public void setWorkflowId(String workflowId) {
        if (workflowId != null) this.workflow = Workflow.builder().id(workflowId).build();
    }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public String getTaskId() { return task != null ? task.getId() : null; }
    public void setTaskId(String taskId) {
        if (taskId != null) this.task = Task.builder().id(taskId).build();
    }

    public String getAgentId() { return agentId; }
    public void setAgentId(String agentId) { this.agentId = agentId; }

    public LogLevel getLogLevel() { return logLevel; }
    public void setLogLevel(LogLevel logLevel) { this.logLevel = logLevel; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
