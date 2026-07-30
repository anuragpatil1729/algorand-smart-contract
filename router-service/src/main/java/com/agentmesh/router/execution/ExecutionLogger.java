package com.agentmesh.router.execution;

import com.agentmesh.router.model.ExecutionLog;
import com.agentmesh.router.model.enums.LogLevel;
import com.agentmesh.router.repository.ExecutionLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ExecutionLogger {

    private static final Logger log = LoggerFactory.getLogger(ExecutionLogger.class);

    private final ExecutionLogRepository logRepository;

    public ExecutionLogger(ExecutionLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logEvent(ExecutionContext context, String taskId, String agentId, LogLevel level, String message) {
        String formatted = String.format("[%s] [wf:%s] [task:%s] [agent:%s] %s",
                level.name(), context != null ? context.getWorkflowId() : "N/A", taskId != null ? taskId : "N/A", agentId != null ? agentId : "N/A", message);

        if (context != null) {
            context.addLog(formatted);
        }

        switch (level) {
            case ERROR:
                log.error(formatted);
                break;
            case WARN:
                log.warn(formatted);
                break;
            default:
                log.info(formatted);
                break;
        }

        if (logRepository != null && context != null && context.getWorkflowId() != null) {
            try {
                ExecutionLog entity = ExecutionLog.builder()
                        .id("log-" + UUID.randomUUID().toString().substring(0, 8))
                        .agentId(agentId)
                        .logLevel(level)
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build();
                logRepository.save(entity);
            } catch (Exception e) {
                log.trace("Execution log entity save trace: {}", e.getMessage());
            }
        }
    }
}
