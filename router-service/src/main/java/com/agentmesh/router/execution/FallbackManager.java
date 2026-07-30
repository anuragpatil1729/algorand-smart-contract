package com.agentmesh.router.execution;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.quote.AgentSelector;
import com.agentmesh.router.quote.QuoteCollector;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import com.agentmesh.router.quote.dto.TaskAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FallbackManager {

    private static final Logger log = LoggerFactory.getLogger(FallbackManager.class);

    private final AgentDiscoveryService discoveryService;
    private final QuoteCollector quoteCollector;
    private final AgentSelector agentSelector;

    public FallbackManager(AgentDiscoveryService discoveryService, QuoteCollector quoteCollector, AgentSelector agentSelector) {
        this.discoveryService = discoveryService;
        this.quoteCollector = quoteCollector;
        this.agentSelector = agentSelector;
    }

    public Optional<TaskAssignment> findFallbackAssignment(TaskAssignment failedAssignment, ExecutionContext context, Set<String> failedAgentIds) {
        if (failedAssignment == null) return Optional.empty();

        String taskId = failedAssignment.getTaskId();
        String capability = failedAssignment.getRequiredCapability();
        String failedAgentId = failedAssignment.getSelectedAgentId();

        if (failedAgentId != null) {
            failedAgentIds.add(failedAgentId);
        }

        log.info("Triggering FallbackManager for task '{}' (capability: '{}', failed agent: '{}')", taskId, capability, failedAgentId);

        // 1. Check existing alternative quotes in current assignment
        if (failedAssignment.getAlternativeAgents() != null && !failedAssignment.getAlternativeAgents().isEmpty()) {
            for (AgentQuoteResponse alt : failedAssignment.getAlternativeAgents()) {
                if (alt.getAgentId() != null && !failedAgentIds.contains(alt.getAgentId())) {
                    log.info("Found pre-collected alternative quote agent '{}' for task '{}'", alt.getAgentId(), taskId);
                    TaskAssignment fallbackAssignment = buildAssignmentFromQuote(failedAssignment, alt, "Assigned fallback agent from pre-collected runner-up quotes after agent '" + failedAgentId + "' failed");
                    return Optional.of(fallbackAssignment);
                }
            }
        }

        // 2. Query Discovery Service for candidate agents supporting the capability
        List<Agent> candidateAgents = discoveryService.findAgentsByCapability(capability);
        List<Agent> availableCandidates = candidateAgents.stream()
                .filter(a -> !failedAgentIds.contains(a.getId()))
                .collect(Collectors.toList());

        if (availableCandidates.isEmpty()) {
            log.warn("No alternative candidate agents available for capability '{}' excluding failed agents {}", capability, failedAgentIds);
            return Optional.empty();
        }

        // 3. Request quotes & score available candidate agents
        PlannedTaskDto taskDto = new PlannedTaskDto();
        taskDto.setTaskId(taskId);
        taskDto.setTaskName(failedAssignment.getTaskName());
        taskDto.setRequiredCapability(capability);

        List<AgentQuoteResponse> quotes = quoteCollector.collectQuotesForTask(taskDto, context.getWorkflowId());
        List<AgentQuoteResponse> validFallbackQuotes = quotes.stream()
                .filter(q -> q.getAgentId() != null && !failedAgentIds.contains(q.getAgentId()))
                .collect(Collectors.toList());

        if (validFallbackQuotes.isEmpty()) {
            log.warn("Failed to collect valid fallback quotes for task '{}'", taskId);
            return Optional.empty();
        }

        TaskAssignment fallbackAssignment = agentSelector.selectAgentForTask(taskDto, validFallbackQuotes, "BALANCED", null);
        fallbackAssignment.setSelectionReason("Assigned dynamic fallback agent '" + fallbackAssignment.getSelectedAgentId() + "' after primary agent '" + failedAgentId + "' failed execution");
        log.info("Successfully re-assigned task '{}' to fallback agent '{}'", taskId, fallbackAssignment.getSelectedAgentId());

        return Optional.of(fallbackAssignment);
    }

    private TaskAssignment buildAssignmentFromQuote(TaskAssignment original, AgentQuoteResponse quote, String reason) {
        TaskAssignment assignment = new TaskAssignment();
        assignment.setTaskId(original.getTaskId());
        assignment.setTaskName(original.getTaskName());
        assignment.setRequiredCapability(original.getRequiredCapability());
        assignment.setSelectedAgentId(quote.getAgentId());
        assignment.setSelectedAgentName(quote.getAgentName());
        assignment.setQuotedPrice(quote.getQuotedPrice());
        assignment.setEstimatedDuration(quote.getEstimatedDuration());
        assignment.setSelectionReason(reason);
        return assignment;
    }
}
