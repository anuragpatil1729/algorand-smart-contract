package com.agentmesh.router.planner;

import com.agentmesh.router.model.Task;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlannerService {

    public List<Task> decomposePrompt(String workflowId, String prompt) {
        String lower = prompt.toLowerCase();
        List<Task> tasks = new ArrayList<>();

        if (lower.contains("pitch deck") || lower.contains("presentation")) {
            tasks.add(buildTask(workflowId, "task-1", "RESEARCH", "Market Research & Competitor Intelligence", "", 1, "MEDIUM"));
            tasks.add(buildTask(workflowId, "task-2", "PITCH_DECK", "Slide Deck Strategy & Financial Model", "task-1", 2, "HIGH"));
            tasks.add(buildTask(workflowId, "task-3", "LOGO_DESIGN", "Brand Identity & Pitch Visual Assets", "task-1", 2, "MEDIUM"));
            tasks.add(buildTask(workflowId, "task-4", "TESTING", "Deck Content Audit & Verification", "task-2,task-3", 3, "LOW"));
        } else if (lower.contains("landing page") || lower.contains("website") || lower.contains("app")) {
            tasks.add(buildTask(workflowId, "task-1", "RESEARCH", "User Experience & Market Research", "", 1, "LOW"));
            tasks.add(buildTask(workflowId, "task-2", "LOGO_DESIGN", "Brand Logo & Graphic Design", "task-1", 2, "MEDIUM"));
            tasks.add(buildTask(workflowId, "task-3", "FRONTEND", "React UI Code Generation", "task-1,task-2", 3, "HIGH"));
            tasks.add(buildTask(workflowId, "task-4", "BACKEND", "REST API & Microservice Specs", "task-1", 3, "MEDIUM"));
            tasks.add(buildTask(workflowId, "task-5", "TESTING", "Full-Stack Security & QA Audit", "task-3,task-4", 4, "LOW"));
        } else if (lower.contains("competitor") || lower.contains("research")) {
            tasks.add(buildTask(workflowId, "task-1", "RESEARCH", "Industry Competitor Deep-Dive", "", 1, "MEDIUM"));
            tasks.add(buildTask(workflowId, "task-2", "PITCH_DECK", "Competitive Landscape Synthesis", "task-1", 2, "MEDIUM"));
            tasks.add(buildTask(workflowId, "task-3", "TESTING", "Data Source Integrity Verification", "task-2", 3, "LOW"));
        } else if (lower.contains("logo") || lower.contains("brand")) {
            tasks.add(buildTask(workflowId, "task-1", "RESEARCH", "Brand Positioning & Target Demographic", "", 1, "LOW"));
            tasks.add(buildTask(workflowId, "task-2", "LOGO_DESIGN", "SVG Logo & Graphic Identity Package", "task-1", 2, "HIGH"));
            tasks.add(buildTask(workflowId, "task-3", "TESTING", "Visual Assets & Format Audit", "task-2", 3, "LOW"));
        } else {
            // Default generic multi-agent workflow
            tasks.add(buildTask(workflowId, "task-1", "RESEARCH", "Domain Research & Requirements Analysis", "", 1, "MEDIUM"));
            tasks.add(buildTask(workflowId, "task-2", "LOGO_DESIGN", "Visual Identity & Architecture Design", "task-1", 2, "MEDIUM"));
            tasks.add(buildTask(workflowId, "task-3", "FRONTEND", "Implementation & Code Synthesis", "task-1,task-2", 3, "HIGH"));
            tasks.add(buildTask(workflowId, "task-4", "TESTING", "Quality Assurance & Algorand Contract Validation", "task-3", 4, "LOW"));
        }

        return tasks;
    }

    private Task buildTask(String workflowId, String taskIdSuffix, String type, String description, String dependencies, int priority, String complexity) {
        String fullTaskId = workflowId + "-" + taskIdSuffix;
        return Task.builder()
                .id(fullTaskId)
                .workflowId(workflowId)
                .taskType(type)
                .description(description)
                .status("PENDING")
                .dependencies(dependencies)
                .priority(priority)
                .estimatedComplexity(complexity)
                .executionTimeMs(0L)
                .build();
    }
}
