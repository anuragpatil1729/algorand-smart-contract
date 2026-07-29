package com.agentmesh.router.service;

import com.agentmesh.router.dto.*;
import com.agentmesh.router.model.*;
import com.agentmesh.router.model.enums.HealthStatus;
import com.agentmesh.router.model.enums.WorkflowStatus;
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
    private final WorkflowEngineService workflowEngine;
    private final AlgorandPaymentService paymentService;

    public AgentMeshService(WorkflowRepository workflowRepository, TaskRepository taskRepository, QuoteRepository quoteRepository, AgentRepository agentRepository, PaymentRepository paymentRepository, TransactionRepository transactionRepository, ExecutionLogRepository logRepository, ScoringConfigRepository scoringConfigRepository, PlannerService plannerService, WorkflowEngineService workflowEngine, AlgorandPaymentService paymentService) {
        this.workflowRepository = workflowRepository;
        this.taskRepository = taskRepository;
        this.quoteRepository = quoteRepository;
        this.agentRepository = agentRepository;
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.logRepository = logRepository;
        this.scoringConfigRepository = scoringConfigRepository;
        this.plannerService = plannerService;
        this.workflowEngine = workflowEngine;
        this.paymentService = paymentService;
    }

    @Transactional
    public WorkflowDto createWorkflow(WorkflowRequestDto request) {
        String workflowId = "wf-" + UUID.randomUUID().toString().substring(0, 8);
        Workflow workflow = Workflow.builder()
                .id(workflowId)
                .prompt(request.getPrompt())
                .status(WorkflowStatus.PENDING_APPROVAL)
                .totalPrice(0.0)
                .createdAt(LocalDateTime.now())
                .build();

        workflowRepository.save(workflow);
        plannerService.decomposeAndQuote(workflow);
        return getWorkflowById(workflowId);
    }

    public WorkflowDto getWorkflowById(String id) {
        Workflow workflow = workflowRepository.findById(id).orElse(null);
        if (workflow == null) return null;

        List<Task> tasks = taskRepository.findByWorkflowId(id);
        List<TaskDto> taskDtos = tasks.stream().map(t -> {
            List<Quote> quotes = quoteRepository.findByTaskId(t.getId());
            List<QuoteDto> quoteDtos = quotes.stream().map(q -> 
                new QuoteDto(q.getId(), q.getAgentId(), q.getAgent() != null ? q.getAgent().getName() : "Agent", q.getPrice(), q.getEstimatedTimeSeconds(), q.getConfidence(), q.getSuccessRate(), q.getRating(), q.getScore(), q.getSelected())
            ).collect(Collectors.toList());

            String agentId = t.getAssignedAgent() != null ? t.getAssignedAgent().getId() : null;
            String deps = t.getDependency() != null ? t.getDependency() : "";
            List<String> depList = Arrays.stream(deps.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());

            return new TaskDto(t.getId(), t.getWorkflowId(), t.getTaskType(), t.getDescription(), agentId, t.getStatus(), t.getPrice(), depList, t.getPriority(), t.getEstimatedComplexity(), t.getExecutionTimeMs(), t.getOutput(), quoteDtos);
        }).collect(Collectors.toList());

        return new WorkflowDto(workflow.getId(), workflow.getPrompt(), workflow.getStatus(), workflow.getTotalPrice(), workflow.getEscrowAddress(), workflow.getEscrowStatus(), workflow.getAggregatedResult(), taskDtos);
    }

    public List<WorkflowDto> getAllWorkflows() {
        return workflowRepository.findAll().stream().map(w -> getWorkflowById(w.getId())).collect(Collectors.toList());
    }

    @Transactional
    public WorkflowDto approveAndExecute(String workflowId) {
        Workflow workflow = workflowRepository.findById(workflowId).orElse(null);
        if (workflow == null) return null;

        workflow.setStatus(WorkflowStatus.APPROVED);
        paymentService.lockFundsInEscrow(workflow);
        workflowEngine.executeWorkflow(workflowId);
        return getWorkflowById(workflowId);
    }

    public List<AgentDto> getAllAgents() {
        return agentRepository.findAll().stream().map(a -> 
            new AgentDto(a.getId(), a.getName(), a.getEndpoint(), a.getWalletAddress(), a.getRating(), a.getSuccessRate(), a.getHealthStatus(), a.getBasePrice(), Arrays.asList(a.getCapabilities().split(",")))
        ).collect(Collectors.toList());
    }

    @Transactional
    public AgentDto registerAgent(AgentDto request) {
        String id = request.getId() != null ? request.getId() : "agent-" + UUID.randomUUID().toString().substring(0, 8);
        String caps = request.getSupportedCapabilities() != null ? String.join(",", request.getSupportedCapabilities()) : "GENERAL";
        Agent agent = Agent.builder()
                .id(id)
                .name(request.getName())
                .endpoint(request.getEndpoint())
                .walletAddress(request.getWalletAddress())
                .rating(request.getRating() != null ? request.getRating() : 4.5)
                .successRate(request.getSuccessRate() != null ? request.getSuccessRate() : 95.0)
                .healthStatus(HealthStatus.UP)
                .basePrice(request.getBasePrice() != null ? request.getBasePrice() : 50.0)
                .capabilities(caps)
                .build();

        agentRepository.save(agent);
        return new AgentDto(agent.getId(), agent.getName(), agent.getEndpoint(), agent.getWalletAddress(), agent.getRating(), agent.getSuccessRate(), agent.getHealthStatus(), agent.getBasePrice(), Arrays.asList(agent.getCapabilities().split(",")));
    }

    public PaymentDetailsDto getPaymentDetails(String workflowId) {
        Payment payment = paymentRepository.findByWorkflowId(workflowId).orElse(null);
        if (payment == null) return null;
        List<Transaction> txs = transactionRepository.findByPaymentId(payment.getId());
        List<PaymentDetailsDto.TransactionDto> txDtos = txs.stream().map(t ->
            new PaymentDetailsDto.TransactionDto(t.getTxHash(), t.getSenderWallet(), t.getReceiverWallet(), t.getAmount(), t.getAgentId(), t.getStatus(), t.getBlockRound(), t.getTimestamp().toString())
        ).collect(Collectors.toList());

        return new PaymentDetailsDto(payment.getId(), payment.getWorkflowId(), payment.getEscrowWallet(), payment.getTotalAmount(), payment.getStatus(), payment.getTxGroupId(), payment.getCreatedAt().toString(), payment.getCompletedAt() != null ? payment.getCompletedAt().toString() : null, txDtos);
    }

    public List<PaymentDetailsDto> getAllPayments() {
        return paymentRepository.findAll().stream().map(p -> getPaymentDetails(p.getWorkflowId())).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public AnalyticsDto getAnalytics() {
        long totalWorkflows = workflowRepository.count();
        long completedWorkflows = workflowRepository.findByStatus(WorkflowStatus.COMPLETED).size();
        long activeAgents = agentRepository.count();

        double totalRevenue = paymentRepository.findAll().stream()
                .filter(p -> "DISBURSED".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(Payment::getTotalAmount)
                .sum();

        List<AnalyticsDto.TaskTypeMetric> taskMetrics = List.of(
                new AnalyticsDto.TaskTypeMetric("RESEARCH", 42),
                new AnalyticsDto.TaskTypeMetric("FRONTEND", 35),
                new AnalyticsDto.TaskTypeMetric("BACKEND", 28),
                new AnalyticsDto.TaskTypeMetric("LOGO_DESIGN", 22),
                new AnalyticsDto.TaskTypeMetric("TESTING", 19)
        );

        return new AnalyticsDto(totalWorkflows, completedWorkflows, activeAgents, Math.round(totalRevenue * 100.0) / 100.0, 98.4, taskMetrics);
    }

    public ScoringConfig getScoringConfig() {
        return scoringConfigRepository.findById("DEFAULT")
                .orElse(ScoringConfig.builder().id("DEFAULT").build());
    }

    @Transactional
    public ScoringConfig updateScoringConfig(ScoringConfig config) {
        config.setId("DEFAULT");
        return scoringConfigRepository.save(config);
    }

    public List<ExecutionLog> getLogs(String workflowId) {
        if (workflowId != null && !workflowId.isBlank()) {
            return logRepository.findByWorkflowIdOrderByTimestampDesc(workflowId);
        }
        return logRepository.findTop100ByOrderByTimestampDesc();
    }
}
