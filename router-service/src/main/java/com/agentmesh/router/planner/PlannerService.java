package com.agentmesh.router.planner;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.model.Task;
import com.agentmesh.router.model.Workflow;
import com.agentmesh.router.model.enums.TaskStatus;
import com.agentmesh.router.model.enums.TaskType;
import com.agentmesh.router.repository.TaskRepository;
import com.agentmesh.router.repository.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlannerService {

    private static final Logger log = LoggerFactory.getLogger(PlannerService.class);

    private final TaskRepository taskRepository;
    private final WorkflowRepository workflowRepository;
    private final AgentDiscoveryService discoveryService;

    public PlannerService(TaskRepository taskRepository, WorkflowRepository workflowRepository, AgentDiscoveryService discoveryService) {
        this.taskRepository = taskRepository;
        this.workflowRepository = workflowRepository;
        this.discoveryService = discoveryService;
    }

    @Transactional
    public List<Task> decomposeAndQuote(Workflow workflow) {
        List<Task> tasks = decomposePrompt(workflow);

        double totalWorkflowPrice = 0.0;
        for (Task task : tasks) {
            discoveryService.collectAndScoreQuotesForTask(task);
            if (task.getPrice() != null) {
                totalWorkflowPrice += task.getPrice();
            }
        }

        workflow.setTotalPrice(Math.round(totalWorkflowPrice * 100.0) / 100.0);
        workflowRepository.save(workflow);

        log.info("Decomposed workflow {} into {} tasks. Total estimated cost: {} Algos", workflow.getId(), tasks.size(), workflow.getTotalPrice());
        return taskRepository.saveAll(tasks);
    }

    private List<Task> decomposePrompt(Workflow workflow) {
        String prompt = workflow.getPrompt().toLowerCase();
        List<Task> tasks = new ArrayList<>();
        String wfId = workflow.getId();

        // 1. Research Task
        Task researchTask = Task.builder()
                .id(wfId + "-task-1")
                .workflow(workflow)
                .taskType(TaskType.RESEARCH)
                .description("User Experience & Market Research")
                .dependency("")
                .priority(1)
                .estimatedComplexity("LOW")
                .status(TaskStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        tasks.add(researchTask);

        if (prompt.contains("pitch") || prompt.contains("deck") || prompt.contains("slide") || prompt.contains("presentation")) {
            Task pptTask = Task.builder()
                    .id(wfId + "-task-2")
                    .workflow(workflow)
                    .taskType(TaskType.PITCH_DECK)
                    .description("Pitch Deck & Business Model Architecture")
                    .dependency("task-1")
                    .priority(2)
                    .estimatedComplexity("MEDIUM")
                    .status(TaskStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            tasks.add(pptTask);
        }

        if (prompt.contains("logo") || prompt.contains("brand") || prompt.contains("graphic") || prompt.contains("landing") || prompt.contains("startup")) {
            Task logoTask = Task.builder()
                    .id(wfId + "-task-3")
                    .workflow(workflow)
                    .taskType(TaskType.LOGO_DESIGN)
                    .description("Brand Logo & Graphic Design")
                    .dependency("task-1")
                    .priority(2)
                    .estimatedComplexity("MEDIUM")
                    .status(TaskStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            tasks.add(logoTask);
        }

        if (prompt.contains("landing") || prompt.contains("page") || prompt.contains("web") || prompt.contains("code") || prompt.contains("app")) {
            Task feTask = Task.builder()
                    .id(wfId + "-task-4")
                    .workflow(workflow)
                    .taskType(TaskType.FRONTEND)
                    .description("React UI Code Generation")
                    .dependency("task-1")
                    .priority(3)
                    .estimatedComplexity("HIGH")
                    .status(TaskStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            tasks.add(feTask);

            Task beTask = Task.builder()
                    .id(wfId + "-task-5")
                    .workflow(workflow)
                    .taskType(TaskType.BACKEND)
                    .description("REST API & Microservice Specs")
                    .dependency("task-1")
                    .priority(3)
                    .estimatedComplexity("MEDIUM")
                    .status(TaskStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            tasks.add(beTask);
        }

        // QA & Security Task
        Task qaTask = Task.builder()
                .id(wfId + "-task-final")
                .workflow(workflow)
                .taskType(TaskType.TESTING)
                .description("Full-Stack Security & QA Audit")
                .dependency("task-1")
                .priority(4)
                .estimatedComplexity("LOW")
                .status(TaskStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        tasks.add(qaTask);

        return tasks;
    }
}
