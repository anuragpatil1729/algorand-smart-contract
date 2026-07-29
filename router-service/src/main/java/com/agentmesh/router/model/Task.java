package com.agentmesh.router.model;

import com.agentmesh.router.model.enums.TaskStatus;
import com.agentmesh.router.model.enums.TaskType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_agent_id")
    private Agent assignedAgent;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType = TaskType.GENERAL;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String dependency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING;

    @Column(name = "execution_time")
    private Long executionTime = 0L;

    @Column(length = 10000)
    private String output;

    private Double price = 0.0;

    private Integer priority = 1;

    @Column(name = "estimated_complexity")
    private String estimatedComplexity = "MEDIUM";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Task() {}

    public Task(String id, Workflow workflow, Agent assignedAgent, TaskType taskType, String description, String dependency, TaskStatus status, Long executionTime, String output, Double price, Integer priority, String estimatedComplexity, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.workflow = workflow;
        this.assignedAgent = assignedAgent;
        this.taskType = taskType != null ? taskType : TaskType.GENERAL;
        this.description = description;
        this.dependency = dependency;
        this.status = status != null ? status : TaskStatus.PENDING;
        this.executionTime = executionTime != null ? executionTime : 0L;
        this.output = output;
        this.price = price != null ? price : 0.0;
        this.priority = priority != null ? priority : 1;
        this.estimatedComplexity = estimatedComplexity != null ? estimatedComplexity : "MEDIUM";
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private Workflow workflow;
        private Agent assignedAgent;
        private TaskType taskType = TaskType.GENERAL;
        private String description;
        private String dependency;
        private TaskStatus status = TaskStatus.PENDING;
        private Long executionTime = 0L;
        private String output;
        private Double price = 0.0;
        private Integer priority = 1;
        private String estimatedComplexity = "MEDIUM";
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;

        public Builder id(String id) { this.id = id; return this; }
        public Builder workflow(Workflow workflow) { this.workflow = workflow; return this; }
        public Builder workflowId(String workflowId) {
            if (workflowId != null) {
                this.workflow = Workflow.builder().id(workflowId).build();
            }
            return this;
        }

        public Builder assignedAgent(Agent assignedAgent) { this.assignedAgent = assignedAgent; return this; }
        public Builder assignedAgent(String agentId) {
            if (agentId != null) {
                this.assignedAgent = Agent.builder().id(agentId).build();
            }
            return this;
        }

        public Builder taskType(TaskType taskType) { this.taskType = taskType; return this; }
        public Builder taskType(String taskTypeStr) {
            if (taskTypeStr != null) {
                try {
                    this.taskType = TaskType.valueOf(taskTypeStr.toUpperCase());
                } catch (Exception e) {
                    this.taskType = TaskType.GENERAL;
                }
            }
            return this;
        }

        public Builder description(String description) { this.description = description; return this; }
        public Builder dependency(String dependency) { this.dependency = dependency; return this; }
        public Builder status(TaskStatus status) { this.status = status; return this; }
        public Builder status(String statusStr) {
            if (statusStr != null) {
                try {
                    this.status = TaskStatus.valueOf(statusStr.toUpperCase());
                } catch (Exception e) {
                    this.status = TaskStatus.PENDING;
                }
            }
            return this;
        }

        public Builder executionTime(Long executionTime) { this.executionTime = executionTime; return this; }
        public Builder output(String output) { this.output = output; return this; }
        public Builder price(Double price) { this.price = price; return this; }
        public Builder priority(Integer priority) { this.priority = priority; return this; }
        public Builder estimatedComplexity(String estimatedComplexity) { this.estimatedComplexity = estimatedComplexity; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder completedAt(LocalDateTime completedAt) { this.completedAt = completedAt; return this; }

        public Task build() {
            return new Task(id, workflow, assignedAgent, taskType, description, dependency, status, executionTime, output, price, priority, estimatedComplexity, createdAt, completedAt);
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

    public Agent getAssignedAgent() { return assignedAgent; }
    public void setAssignedAgent(Agent assignedAgent) { this.assignedAgent = assignedAgent; }

    public String getAssignedAgentId() { return assignedAgent != null ? assignedAgent.getId() : null; }
    public void setAssignedAgent(String agentId) {
        if (agentId != null) this.assignedAgent = Agent.builder().id(agentId).build();
    }

    public TaskType getTaskTypeEnum() { return taskType; }
    public String getTaskType() { return taskType != null ? taskType.name() : "GENERAL"; }
    public void setTaskType(TaskType taskType) { this.taskType = taskType; }
    public void setTaskType(String taskTypeStr) {
        if (taskTypeStr != null) {
            try {
                this.taskType = TaskType.valueOf(taskTypeStr.toUpperCase());
            } catch (Exception e) {
                this.taskType = TaskType.GENERAL;
            }
        }
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDependency() { return dependency; }
    public void setDependency(String dependency) { this.dependency = dependency; }

    public String getDependencies() { return dependency; }
    public void setDependencies(String dependencies) { this.dependency = dependencies; }

    public TaskStatus getStatusEnum() { return status; }
    public String getStatus() { return status != null ? status.name() : "PENDING"; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public void setStatus(String statusStr) {
        if (statusStr != null) {
            try {
                this.status = TaskStatus.valueOf(statusStr.toUpperCase());
            } catch (Exception e) {
                this.status = TaskStatus.PENDING;
            }
        }
    }

    public Long getExecutionTime() { return executionTime; }
    public void setExecutionTime(Long executionTime) { this.executionTime = executionTime; }

    public Long getExecutionTimeMs() { return executionTime; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTime = executionTimeMs; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getEstimatedComplexity() { return estimatedComplexity; }
    public void setEstimatedComplexity(String estimatedComplexity) { this.estimatedComplexity = estimatedComplexity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
