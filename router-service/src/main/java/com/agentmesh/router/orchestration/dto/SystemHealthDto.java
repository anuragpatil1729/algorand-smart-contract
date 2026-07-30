package com.agentmesh.router.orchestration.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class SystemHealthDto {

    private String overallStatus = "HEALTHY";
    private Map<String, String> components = new LinkedHashMap<>();
    private Long timestamp = System.currentTimeMillis();

    public SystemHealthDto() {
        components.put("planner", "UP");
        components.put("registry", "UP");
        components.put("discovery", "UP");
        components.put("quoteEngine", "UP");
        components.put("executionEngine", "UP");
        components.put("x402Middleware", "UP");
        components.put("algorandProvider", "UP");
    }

    public String getOverallStatus() { return overallStatus; }
    public void setOverallStatus(String overallStatus) { this.overallStatus = overallStatus; }

    public Map<String, String> getComponents() { return components; }
    public void setComponents(Map<String, String> components) { this.components = components; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
