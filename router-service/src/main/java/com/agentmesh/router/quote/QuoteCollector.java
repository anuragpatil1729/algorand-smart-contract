package com.agentmesh.router.quote;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.planner.dto.PlannedTaskDto;
import com.agentmesh.router.planner.dto.WorkflowPlanResponseDto;
import com.agentmesh.router.quote.dto.AgentQuoteRequest;
import com.agentmesh.router.quote.dto.AgentQuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class QuoteCollector {

    private static final Logger log = LoggerFactory.getLogger(QuoteCollector.class);

    private final AgentDiscoveryService discoveryService;
    private final QuoteRequestBuilder requestBuilder;
    private final QuoteResponseParser responseParser;
    private final QuoteValidator quoteValidator;
    private final QuoteCache quoteCache;
    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final long requestTimeoutMs;
    private final int maxRetries;

    public QuoteCollector(
            AgentDiscoveryService discoveryService,
            QuoteRequestBuilder requestBuilder,
            QuoteResponseParser responseParser,
            QuoteValidator quoteValidator,
            QuoteCache quoteCache,
            RestTemplate restTemplate,
            @Value("${agentmesh.quote.timeout-ms:2000}") long requestTimeoutMs,
            @Value("${agentmesh.quote.max-retries:1}") int maxRetries
    ) {
        this.discoveryService = discoveryService;
        this.requestBuilder = requestBuilder;
        this.responseParser = responseParser;
        this.quoteValidator = quoteValidator;
        this.quoteCache = quoteCache;
        this.restTemplate = restTemplate;
        this.requestTimeoutMs = requestTimeoutMs;
        this.maxRetries = maxRetries;
        this.executorService = Executors.newFixedThreadPool(20);
    }

    public Map<String, List<AgentQuoteResponse>> collectQuotesForWorkflow(WorkflowPlanResponseDto workflowPlan) {
        if (workflowPlan == null || workflowPlan.getTaskList() == null) {
            return Collections.emptyMap();
        }

        String workflowId = workflowPlan.getWorkflowId() != null ? workflowPlan.getWorkflowId() : UUID.randomUUID().toString();
        Map<String, List<AgentQuoteResponse>> workflowQuotesMap = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> taskFutures = workflowPlan.getTaskList().stream()
                .map(task -> CompletableFuture.runAsync(() -> {
                    List<AgentQuoteResponse> taskQuotes = collectQuotesForTask(task, workflowId);
                    workflowQuotesMap.put(task.getTaskId(), taskQuotes);
                }, executorService))
                .collect(Collectors.toList());

        try {
            CompletableFuture.allOf(taskFutures.toArray(new CompletableFuture[0]))
                    .get(requestTimeoutMs * 2 + 1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.warn("Workflow quote collection timed out or encountered errors: {}", e.getMessage());
        }

        return workflowQuotesMap;
    }

    public List<AgentQuoteResponse> collectQuotesForTask(PlannedTaskDto task, String workflowId) {
        if (task == null) return Collections.emptyList();

        List<Agent> candidateAgents = discoveryService.findAgentsByCapability(task.getRequiredCapability());
        if (candidateAgents == null || candidateAgents.isEmpty()) {
            candidateAgents = discoveryService.discoverAllAgents();
        }

        List<CompletableFuture<AgentQuoteResponse>> futures = new ArrayList<>();

        for (Agent agent : candidateAgents) {
            // Cache Check
            Optional<AgentQuoteResponse> cached = quoteCache.get(task.getTaskId(), agent.getId());
            if (cached.isPresent()) {
                log.info("Found cached quote for task '{}' and agent '{}'", task.getTaskId(), agent.getId());
                futures.add(CompletableFuture.completedFuture(cached.get()));
                continue;
            }

            CompletableFuture<AgentQuoteResponse> future = CompletableFuture.supplyAsync(
                    () -> requestQuoteWithRetryAndFallback(agent, task, workflowId),
                    executorService
            );
            futures.add(future);
        }

        List<AgentQuoteResponse> quotes = new ArrayList<>();
        for (CompletableFuture<AgentQuoteResponse> future : futures) {
            try {
                AgentQuoteResponse response = future.get(requestTimeoutMs + 500, TimeUnit.MILLISECONDS);
                if (response != null) {
                    if (quoteValidator.validate(response)) {
                        quoteCache.put(task.getTaskId(), response.getAgentId(), response);
                    }
                    quotes.add(response);
                }
            } catch (TimeoutException te) {
                log.warn("Timeout collecting quote for task '{}'", task.getTaskId());
            } catch (Exception e) {
                log.warn("Error collecting quote for task '{}': {}", task.getTaskId(), e.getMessage());
            }
        }

        return quotes;
    }

    private AgentQuoteResponse requestQuoteWithRetryAndFallback(Agent agent, PlannedTaskDto task, String workflowId) {
        AgentQuoteRequest request = requestBuilder.buildQuoteRequest(task, workflowId);
        String quoteUrl = agent.getEndpoint() + "/quote";

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<AgentQuoteRequest> entity = new HttpEntity<>(request, headers);

                Map<String, Object> rawMap = restTemplate.postForObject(quoteUrl, entity, Map.class);
                if (rawMap != null) {
                    AgentQuoteResponse response = responseParser.parseFromMap(rawMap, agent, task.getTaskId(), workflowId);
                    return response;
                }
            } catch (Exception e) {
                log.warn("Attempt {}/{} failed for agent {} at {}: {}", attempt + 1, maxRetries + 1, agent.getId(), quoteUrl, e.getMessage());
            }
        }

        // Dynamic Fallback on Failure/Timeout
        return generateFallbackQuote(agent, task, workflowId);
    }

    private AgentQuoteResponse generateFallbackQuote(Agent agent, PlannedTaskDto task, String workflowId) {
        log.info("Generating dynamic fallback quote for agent '{}' (task '{}')", agent.getId(), task.getTaskId());
        double complexityFactor = "HIGH".equalsIgnoreCase(task.getComplexity()) ? 1.4 :
                ("LOW".equalsIgnoreCase(task.getComplexity()) ? 0.8 : 1.0);

        double basePrice = agent.getBasePrice() != null ? agent.getBasePrice() : 50.0;
        double price = Math.round((basePrice * complexityFactor + (Math.random() * 5.0)) * 100.0) / 100.0;
        int eta = (int) (10 * complexityFactor + (Math.random() * 4));

        AgentQuoteResponse fallback = new AgentQuoteResponse();
        fallback.setAgentId(agent.getId());
        fallback.setAgentName(agent.getName());
        fallback.setTaskId(task.getTaskId());
        fallback.setWorkflowId(workflowId);
        fallback.setCapability(task.getRequiredCapability());
        fallback.setQuotedPrice(price);
        fallback.setEstimatedDuration(eta);
        fallback.setConfidence(Math.round((92.0 + Math.random() * 6.0) * 10.0) / 10.0);
        fallback.setCurrentLoad(agent.getCurrentLoad() != null ? agent.getCurrentLoad() : 10.0);
        fallback.setCurrentQueueLength(0);
        fallback.setAverageResponseTime(45.0);
        fallback.setSuccessRate(agent.getSuccessRate() != null ? agent.getSuccessRate() : 97.0);
        fallback.setReputation(agent.getRating() != null ? agent.getRating() : 4.8);
        fallback.setHealthScore(agent.getHealthScore() != null ? agent.getHealthScore() : 100.0);
        fallback.setValid(true);
        fallback.setStatus("FALLBACK_ESTIMATED");

        return fallback;
    }
}
