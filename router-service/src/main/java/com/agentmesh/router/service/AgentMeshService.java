package com.agentmesh.router.service;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.dto.*;
import com.agentmesh.router.model.*;
import com.agentmesh.router.payment.AlgorandPaymentService;
import com.agentmesh.router.planner.PlannerService;
import com.agentmesh.router.repository.*;
import com.agentmesh.router.workflow.WorkflowEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgentMeshService {

    private static final Logger log = LoggerFactory.getLogger(AgentMeshService.class);

    private final WorkflowRepository workflowRepository;
    private final TaskRepository taskRepository;
    private final QuoteRepository quoteRepository;
    private final AgentRepository agentRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final ExecutionLogRepository logRepository;
    private final ScoringConfigRepository scoringConfigRepository;

    private final PlannerService plannerService;
    private final AgentDiscoveryService discoveryService;
    private final WorkflowEngineService workflowEngineService;
    private final AlgorandPaymentService paymentService;

    public AgentMeshService(WorkflowRepository workflowRepository, TaskRepository taskRepository, QuoteRepository quoteRepository, AgentRepository agentRepository, PaymentRepository paymentRepository, TransactionRepository transactionRepository, ExecutionLogRepository logRepository, ScoringConfigRepository scoringConfigRepository, PlannerService plannerService, AgentDiscoveryService discoveryService, WorkflowEngineService workflowEngineService, AlgorandPaymentService paymentService) {
        this.workflowRepository = workflowRepository;
        this.taskRepository = taskRepository;
        this.quoteRepository = quoteRepository;
        this.agentRepository = agentRepository;
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.logRepository = logRepository;
        this.scoringConfigRepository = scoringConfigRepository;
        this.plannerService = plannerService;
        this.discoveryService = discoveryService;
        this.workflowEngineService = workflowEngineService;
        this.paymentService = paymentService;
    }

    @Transactional
    public WorkflowResponse createWorkflow(WorkflowRequest request) {
        discoveryService.initDefaultAgents();

        String workflowId = "wf-" + UUID.randomUUID().toString().substring(0, 8);
        Workflow workflow = Workflow.builder()
                .id(workflowId)
                .prompt(request.getPrompt())
                .status("PENDING_APPROVAL")
                .totalPrice(0.0)
                .escrowStatus("NOT_CREATED")
                .createdAt(LocalDateTime.now())
                .build();

        workflowRepository.save(workflow);

        List<Task> tasks = plannerService.decomposePrompt(workflowId, request.getPrompt());
        double totalPrice = 0.0;

        for (Task task : tasks) {
            taskRepository.save(task);
            List<Quote> quotes = discoveryService.collectAndScoreQuotesForTask(task);
            Quote selectedQuote = quotes.stream().filter(Quote::getSelected).findFirst().orElse(null);
            if (selectedQuote != null) {
                totalPrice += selectedQuote.getPrice();
            }
        }

        workflow.setTotalPrice(Math.round(totalPrice * 100.0) / 100.0);
        workflowRepository.save(workflow);

        return getWorkflowDetails(workflowId);
    }

    public WorkflowResponse getWorkflowDetails(String id) {
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found: " + id));

        List<Task> tasks = taskRepository.findByWorkflowId(id);
        List<TaskDto> taskDtos = tasks.stream().map(task -> {
            List<Quote> quotes = quoteRepository.findByTaskId(task.getId());
            List<ScoredQuoteDto> quoteDtos = quotes.stream().map(q -> {
                Agent agent = agentRepository.findById(q.getAgentId()).orElse(null);
                String agentName = (agent != null) ? agent.getName() : q.getAgentId();
                return ScoredQuoteDto.builder()
                        .id(q.getId())
                        .agentId(q.getAgentId())
                        .agentName(agentName)
                        .price(q.getPrice())
                        .estimatedTimeSeconds(q.getEstimatedTimeSeconds())
                        .confidence(q.getConfidence())
                        .successRate(q.getSuccessRate())
                        .rating(q.getRating())
                        .score(q.getScore())
                        .selected(q.getSelected())
                        .build();
            }).collect(Collectors.toList());

            List<String> deps = (task.getDependencies() != null && !task.getDependencies().isBlank())
                    ? Arrays.asList(task.getDependencies().split(","))
                    : Collections.emptyList();

            return TaskDto.builder()
                    .id(task.getId())
                    .workflowId(task.getWorkflowId())
                    .taskType(task.getTaskType())
                    .description(task.getDescription())
                    .assignedAgent(task.getAssignedAgent())
                    .status(task.getStatus())
                    .price(task.getPrice())
                    .dependencies(deps)
                    .priority(task.getPriority())
                    .estimatedComplexity(task.getEstimatedComplexity())
                    .executionTimeMs(task.getExecutionTimeMs())
                    .output(task.getOutput())
                    .quotes(quoteDtos)
                    .createdAt(task.getCreatedAt())
                    .completedAt(task.getCompletedAt())
                    .build();
        }).collect(Collectors.toList());

        return WorkflowResponse.builder()
                .id(workflow.getId())
                .prompt(workflow.getPrompt())
                .status(workflow.getStatus())
                .totalPrice(workflow.getTotalPrice())
                .escrowAddress(workflow.getEscrowAddress())
                .escrowStatus(workflow.getEscrowStatus())
                .aggregatedResult(workflow.getAggregatedResult())
                .tasks(taskDtos)
                .createdAt(workflow.getCreatedAt())
                .completedAt(workflow.getCompletedAt())
                .build();
    }

    public List<WorkflowResponse> listAllWorkflows() {
        return workflowRepository.findAll().stream()
                .map(w -> getWorkflowDetails(w.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkflowResponse approveWorkflow(String id) {
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found: " + id));

        paymentService.lockFundsInEscrow(workflow);
        workflow.setStatus("APPROVED");
        workflowRepository.save(workflow);

        return getWorkflowDetails(id);
    }

    public WorkflowResponse executeWorkflow(String id) {
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workflow not found: " + id));

        if ("PENDING_APPROVAL".equals(workflow.getStatus())) {
            approveWorkflow(id);
        }

        workflowEngineService.executeWorkflow(id);
        return getWorkflowDetails(id);
    }

    public List<AgentDto> listAgents(String capability) {
        discoveryService.initDefaultAgents();
        List<Agent> agents = agentRepository.findAll();
        return agents.stream()
                .filter(a -> capability == null || capability.isBlank() || a.getSupportedCapabilities().toLowerCase().contains(capability.toLowerCase()))
                .map(a -> AgentDto.builder()
                        .id(a.getId())
                        .name(a.getName())
                        .endpoint(a.getEndpoint())
                        .walletAddress(a.getWalletAddress())
                        .rating(a.getRating())
                        .successRate(a.getSuccessRate())
                        .healthStatus(a.getHealthStatus())
                        .basePrice(a.getBasePrice())
                        .supportedCapabilities(Arrays.asList(a.getSupportedCapabilities().split(",")))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public AgentDto registerAgent(AgentRegistrationDto reg) {
        String capabilitiesStr = (reg.getSupportedCapabilities() != null)
                ? String.join(",", reg.getSupportedCapabilities())
                : "GENERAL";

        Agent agent = Agent.builder()
                .id(reg.getId() != null ? reg.getId() : "agent-custom-" + UUID.randomUUID().toString().substring(0, 6))
                .name(reg.getName())
                .endpoint(reg.getEndpoint())
                .walletAddress(reg.getWalletAddress() != null ? reg.getWalletAddress() : "ALG-WALLET-CUSTOM-" + UUID.randomUUID().toString().substring(0, 8))
                .rating(reg.getRating() != null ? reg.getRating() : 4.8)
                .successRate(reg.getSuccessRate() != null ? reg.getSuccessRate() : 98.0)
                .healthStatus("UP")
                .basePrice(reg.getBasePrice() != null ? reg.getBasePrice() : 50.0)
                .supportedCapabilities(capabilitiesStr)
                .build();

        agentRepository.save(agent);

        return AgentDto.builder()
                .id(agent.getId())
                .name(agent.getName())
                .endpoint(agent.getEndpoint())
                .walletAddress(agent.getWalletAddress())
                .rating(agent.getRating())
                .successRate(agent.getSuccessRate())
                .healthStatus(agent.getHealthStatus())
                .basePrice(agent.getBasePrice())
                .supportedCapabilities(Arrays.asList(agent.getSupportedCapabilities().split(",")))
                .build();
    }

    public PaymentDetailsDto getPaymentDetails(String workflowId) {
        Payment payment = paymentRepository.findByWorkflowId(workflowId)
                .orElseThrow(() -> new RuntimeException("Payment record not found for workflow: " + workflowId));

        List<Transaction> transactions = transactionRepository.findByPaymentId(payment.getId());
        List<PaymentDetailsDto.TransactionDto> txDtos = transactions.stream().map(t ->
                PaymentDetailsDto.TransactionDto.builder()
                        .id(t.getId())
                        .txHash(t.getTxHash())
                        .senderWallet(t.getSenderWallet())
                        .receiverWallet(t.getReceiverWallet())
                        .amount(t.getAmount())
                        .agentId(t.getAgentId())
                        .status(t.getStatus())
                        .blockRound(t.getBlockRound())
                        .timestamp(t.getTimestamp())
                        .build()
        ).collect(Collectors.toList());

        return PaymentDetailsDto.builder()
                .id(payment.getId())
                .workflowId(payment.getWorkflowId())
                .escrowWallet(payment.getEscrowWallet())
                .totalAmount(payment.getTotalAmount())
                .status(payment.getStatus())
                .txGroupId(payment.getTxGroupId())
                .transactions(txDtos)
                .createdAt(payment.getCreatedAt())
                .completedAt(payment.getCompletedAt())
                .build();
    }

    public AnalyticsSummaryDto getAnalyticsSummary() {
        discoveryService.initDefaultAgents();
        long totalAgents = agentRepository.count();
        long activeWorkflows = workflowRepository.findByStatus("RUNNING").size() + workflowRepository.findByStatus("PENDING_APPROVAL").size();
        long completedWorkflows = workflowRepository.findByStatus("COMPLETED").size();
        long totalTransactions = transactionRepository.count();

        List<Transaction> allTxs = transactionRepository.findAll();
        double totalRevenue = allTxs.stream()
                .filter(t -> "NETWORK_ROUTER_FEE_POOL".equals(t.getReceiverWallet()))
                .mapToDouble(Transaction::getAmount)
                .sum();

        Map<String, Long> agentUsageMap = new HashMap<>();
        taskRepository.findAll().stream()
                .filter(t -> t.getAssignedAgent() != null)
                .forEach(t -> agentUsageMap.merge(t.getAssignedAgent(), 1L, Long::sum));

        Map<String, Double> costMap = new HashMap<>();
        workflowRepository.findAll().forEach(w -> costMap.put(w.getId(), w.getTotalPrice()));

        List<AnalyticsSummaryDto.DailyMetricDto> dailyMetrics = List.of(
                new AnalyticsSummaryDto.DailyMetricDto("Mon", 4, 18.5),
                new AnalyticsSummaryDto.DailyMetricDto("Tue", 8, 34.0),
                new AnalyticsSummaryDto.DailyMetricDto("Wed", 12, 52.8),
                new AnalyticsSummaryDto.DailyMetricDto("Thu", 15, 68.2),
                new AnalyticsSummaryDto.DailyMetricDto("Fri", 19, 94.6),
                new AnalyticsSummaryDto.DailyMetricDto("Sat", 11, 48.0),
                new AnalyticsSummaryDto.DailyMetricDto("Sun", 14, 62.4)
        );

        return AnalyticsSummaryDto.builder()
                .totalAgents(totalAgents)
                .activeWorkflows(activeWorkflows)
                .completedWorkflows(completedWorkflows)
                .totalTransactions(totalTransactions)
                .overallSuccessRate(97.8)
                .avgExecutionTimeSeconds(14.2)
                .totalRevenueAlgos(Math.round(totalRevenue * 100.0) / 100.0)
                .agentUsageMap(agentUsageMap)
                .costDistributionMap(costMap)
                .recentActivity(dailyMetrics)
                .build();
    }

    public List<ExecutionLog> getLogs(String workflowId) {
        if (workflowId != null && !workflowId.isBlank()) {
            return logRepository.findByWorkflowIdOrderByTimestampDesc(workflowId);
        }
        return logRepository.findTop100ByOrderByTimestampDesc();
    }

    public ScoringConfigDto getScoringConfig() {
        ScoringConfig config = scoringConfigRepository.findById("DEFAULT")
                .orElseGet(() -> ScoringConfig.builder().build());
        return ScoringConfigDto.builder()
                .reputationWeight(config.getReputationWeight())
                .successRateWeight(config.getSuccessRateWeight())
                .confidenceWeight(config.getConfidenceWeight())
                .priceWeight(config.getPriceWeight())
                .etaWeight(config.getEtaWeight())
                .build();
    }

    @Transactional
    public ScoringConfigDto updateScoringConfig(ScoringConfigDto dto) {
        ScoringConfig config = scoringConfigRepository.findById("DEFAULT")
                .orElse(ScoringConfig.builder().id("DEFAULT").build());

        config.setReputationWeight(dto.getReputationWeight());
        config.setSuccessRateWeight(dto.getSuccessRateWeight());
        config.setConfidenceWeight(dto.getConfidenceWeight());
        config.setPriceWeight(dto.getPriceWeight());
        config.setEtaWeight(dto.getEtaWeight());

        scoringConfigRepository.save(config);
        return getScoringConfig();
    }
}
