package com.agentmesh.router.service;

import com.agentmesh.router.model.Agent;
import com.agentmesh.router.model.enums.HealthStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class HealthMonitorService {

    public double calculateHealthScore(Agent agent) {
        if (agent == null) return 0.0;

        double score = 100.0;

        // 1. Heartbeat Freshness Penalty
        if (agent.getLastHeartbeat() != null) {
            long secondsSinceHeartbeat = Duration.between(agent.getLastHeartbeat(), LocalDateTime.now()).getSeconds();
            if (secondsSinceHeartbeat > 90) {
                return 0.0; // Offline
            } else if (secondsSinceHeartbeat > 45) {
                score -= 40.0;
            } else if (secondsSinceHeartbeat > 30) {
                score -= 15.0;
            }
        } else {
            score -= 50.0;
        }

        // 2. CPU Penalty
        if (agent.getCpuUsage() != null) {
            if (agent.getCpuUsage() > 90.0) {
                score -= 25.0;
            } else if (agent.getCpuUsage() > 75.0) {
                score -= 10.0;
            }
        }

        // 3. Queue Size Penalty
        if (agent.getQueueSize() != null && agent.getQueueSize() > 0) {
            score -= Math.min(30.0, agent.getQueueSize() * 5.0);
        }

        // 4. Response Time Penalty
        if (agent.getAverageResponseTime() != null && agent.getAverageResponseTime() > 500.0) {
            score -= 15.0;
        }

        return Math.max(0.0, Math.round(score * 10.0) / 10.0);
    }

    public HealthStatus evaluateStatus(Agent agent) {
        if (agent == null) return HealthStatus.OFFLINE;

        if (agent.getLastHeartbeat() != null) {
            long secondsSinceHeartbeat = Duration.between(agent.getLastHeartbeat(), LocalDateTime.now()).getSeconds();
            if (secondsSinceHeartbeat > 90) {
                return HealthStatus.OFFLINE;
            }
        }

        double load = agent.getCurrentLoad() != null ? agent.getCurrentLoad() : 0.0;
        double healthScore = agent.getHealthScore() != null ? agent.getHealthScore() : 100.0;

        if (healthScore < 40.0) {
            return HealthStatus.DEGRADED;
        }

        if (load >= 90.0 || (agent.getRunningTasks() != null && agent.getMaxConcurrency() != null && agent.getRunningTasks() >= agent.getMaxConcurrency())) {
            return HealthStatus.BUSY;
        }

        return HealthStatus.ONLINE;
    }
}
