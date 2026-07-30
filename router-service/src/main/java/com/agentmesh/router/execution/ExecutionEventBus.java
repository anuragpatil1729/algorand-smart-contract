package com.agentmesh.router.execution;

import com.agentmesh.router.execution.dto.ExecutionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Component
public class ExecutionEventBus {

    private static final Logger log = LoggerFactory.getLogger(ExecutionEventBus.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final List<Consumer<ExecutionEvent>> eventListeners = new CopyOnWriteArrayList<>();

    public ExecutionEventBus(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void registerListener(Consumer<ExecutionEvent> listener) {
        if (listener != null) {
            eventListeners.add(listener);
        }
    }

    public void publishEvent(ExecutionEvent event, ExecutionContext context) {
        if (event == null) return;

        if (context != null) {
            context.addEvent(event);
            context.addLog("[" + event.getEventType() + "] " + event.getMessage());
        }

        log.info("Execution Event published: {} for workflow '{}' (task: '{}')", event.getEventType(), event.getWorkflowId(), event.getTaskId());

        // Notify internal in-memory subscribers
        for (Consumer<ExecutionEvent> listener : eventListeners) {
            try {
                listener.accept(event);
            } catch (Exception e) {
                log.warn("Error in event listener execution: {}", e.getMessage());
            }
        }

        // Broadcast to WebSocket topics
        if (messagingTemplate != null && event.getWorkflowId() != null) {
            try {
                messagingTemplate.convertAndSend("/topic/execution/" + event.getWorkflowId(), event);
                messagingTemplate.convertAndSend("/topic/execution-events", event);
            } catch (Exception e) {
                log.trace("WebSocket send message trace: {}", e.getMessage());
            }
        }
    }
}
