package com.agentmesh.router.orchestration.dto;

import java.util.Map;

public class SystemMetricsDto {

    private Map<String, Object> executionMetrics;
    private Map<String, Object> paymentMetrics;
    private Long registeredAgentsCount;
    private Long timestamp = System.currentTimeMillis();

    public SystemMetricsDto() {}

    public SystemMetricsDto(Map<String, Object> executionMetrics, Map<String, Object> paymentMetrics, Long registeredAgentsCount) {
        this.executionMetrics = executionMetrics;
        this.paymentMetrics = paymentMetrics;
        this.registeredAgentsCount = registeredAgentsCount;
    }

    public Map<String, Object> getExecutionMetrics() { return executionMetrics; }
    public void setExecutionMetrics(Map<String, Object> executionMetrics) { this.executionMetrics = executionMetrics; }

    public Map<String, Object> getPaymentMetrics() { return paymentMetrics; }
    public void setPaymentMetrics(Map<String, Object> paymentMetrics) { this.paymentMetrics = paymentMetrics; }

    public Long getRegisteredAgentsCount() { return registeredAgentsCount; }
    public void setRegisteredAgentsCount(Long registeredAgentsCount) { this.registeredAgentsCount = registeredAgentsCount; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
