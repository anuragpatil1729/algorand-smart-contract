package com.agentmesh.router.execution.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class ExecutionTaskResponse {

    @JsonProperty("executionId")
    @JsonAlias({"executionId", "execution_id"})
    private String executionId;

    @JsonProperty("taskId")
    @JsonAlias({"taskId", "task_id"})
    private String taskId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("output")
    private Object output;

    @JsonProperty("executionTimeMs")
    @JsonAlias({"executionTimeMs", "execution_time_ms", "executionTime", "execution_time"})
    private Long executionTimeMs = 0L;

    @JsonProperty("logs")
    private List<String> logs;

    @JsonProperty("metrics")
    private Map<String, Object> metrics;

    @JsonProperty("error")
    private String error;

    public ExecutionTaskResponse() {}

    public ExecutionTaskResponse(String executionId, String taskId, String status, Object output, Long executionTimeMs, String error) {
        this.executionId = executionId;
        this.taskId = taskId;
        this.status = status;
        this.output = output;
        this.executionTimeMs = executionTimeMs;
        this.error = error;
    }

    public String getExecutionId() { return executionId; }
    public void setExecutionId(String executionId) { this.executionId = executionId; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Object getOutput() { return output; }
    public void setOutput(Object output) { this.output = output; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public List<String> getLogs() { return logs; }
    public void setLogs(List<String> logs) { this.logs = logs; }

    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
