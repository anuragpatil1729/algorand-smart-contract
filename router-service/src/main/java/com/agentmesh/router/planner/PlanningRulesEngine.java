package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PlanningRulesEngine {

    private final CapabilityResolver capabilityResolver;
    private final TaskComplexityEstimator estimator;

    public PlanningRulesEngine(CapabilityResolver capabilityResolver, TaskComplexityEstimator estimator) {
        this.capabilityResolver = capabilityResolver;
        this.estimator = estimator;
    }

    public List<PlannedTaskDto> generateTasks(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return Collections.emptyList();
        }

        String lowerPrompt = prompt.toLowerCase(Locale.ROOT);
        List<PlannedTaskDto> tasks = new ArrayList<>();

        // 1. Mandatory Baseline Research Task
        PlannedTaskDto researchTask = createBaseTask("task-research", "Market & User Domain Research", "Research target domain user stories and market requirements", "RESEARCH", 1, Collections.emptyList());
        tasks.add(researchTask);

        // 2. Presentation / Pitch Deck Rule
        if (lowerPrompt.contains("pitch") || lowerPrompt.contains("deck") || lowerPrompt.contains("slide") || lowerPrompt.contains("presentation")) {
            PlannedTaskDto pptTask = createBaseTask("task-presentation", "Pitch Deck Strategy & Slide Generation", "Generate executive pitch deck slides", "PRESENTATION_GENERATION", 2, List.of("task-research"));
            tasks.add(pptTask);
        }

        // 3. Logo / Branding / Visual Design Rule
        if (lowerPrompt.contains("logo") || lowerPrompt.contains("brand") || lowerPrompt.contains("graphic") || lowerPrompt.contains("landing page") || lowerPrompt.contains("startup") || lowerPrompt.contains("website")) {
            PlannedTaskDto logoTask = createBaseTask("task-logo", "Brand Identity & Vector Graphics", "Create vector SVG logos and brand color guidelines", "IMAGE_GENERATION", 2, List.of("task-research"));
            tasks.add(logoTask);
        }

        // 4. Web / Frontend UI Rule
        boolean hasFrontend = lowerPrompt.contains("landing page") || lowerPrompt.contains("website") || lowerPrompt.contains("frontend") || lowerPrompt.contains("ui") || lowerPrompt.contains("app");
        if (hasFrontend) {
            List<String> feDeps = new ArrayList<>();
            feDeps.add("task-research");
            if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-logo"))) {
                feDeps.add("task-logo");
            }
            PlannedTaskDto feTask = createBaseTask("task-frontend", "Frontend UI Component", "Build React UI components and landing page", "FRONTEND_DEVELOPMENT", 3, feDeps);
            tasks.add(feTask);
        }

        // 5. Backend API Rule
        boolean hasBackend = lowerPrompt.contains("backend") || lowerPrompt.contains("api") || lowerPrompt.contains("service") || lowerPrompt.contains("app") || lowerPrompt.contains("landing page") || lowerPrompt.contains("authentication") || lowerPrompt.contains("database");
        if (hasBackend) {
            PlannedTaskDto beTask = createBaseTask("task-backend", "Backend REST API Microservices", "Implement REST API controllers and service logic", "BACKEND_DEVELOPMENT", 3, List.of("task-research"));
            tasks.add(beTask);
        }

        // 6. Database Schema Rule
        if (lowerPrompt.contains("database") || lowerPrompt.contains("db") || lowerPrompt.contains("schema") || lowerPrompt.contains("sql") || lowerPrompt.contains("app")) {
            List<String> dbDeps = new ArrayList<>();
            if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-backend"))) {
                dbDeps.add("task-backend");
            } else {
                dbDeps.add("task-research");
            }
            PlannedTaskDto dbTask = createBaseTask("task-database", "Database Schema & Entity Models", "Design relational database schema and JPA entities", "DATABASE_DESIGN", 4, dbDeps);
            tasks.add(dbTask);
        }

        // 7. Authentication Rule
        if (lowerPrompt.contains("authentication") || lowerPrompt.contains("auth") || lowerPrompt.contains("login") || lowerPrompt.contains("jwt")) {
            List<String> authDeps = new ArrayList<>();
            if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-backend"))) {
                authDeps.add("task-backend");
            }
            if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-database"))) {
                authDeps.add("task-database");
            }
            if (authDeps.isEmpty()) authDeps.add("task-research");

            PlannedTaskDto authTask = createBaseTask("task-auth", "Authentication & Authorization Security", "Implement JWT auth and security filter chains", "AUTHENTICATION", 5, authDeps);
            tasks.add(authTask);
        }

        // 8. Testing & QA Audit Rule
        List<String> testingDeps = new ArrayList<>();
        if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-frontend"))) testingDeps.add("task-frontend");
        if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-auth"))) testingDeps.add("task-auth");
        if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-backend")) && testingDeps.isEmpty()) testingDeps.add("task-backend");
        if (testingDeps.isEmpty()) testingDeps.add("task-research");

        PlannedTaskDto qaTask = createBaseTask("task-testing", "Automated QA & Security Audit", "Execute test suites and security vulnerability audit", "TESTING", 6, testingDeps);
        tasks.add(qaTask);

        // 9. Deployment Rule
        if (lowerPrompt.contains("deploy") || lowerPrompt.contains("deployment") || lowerPrompt.contains("docker") || lowerPrompt.contains("cloud") || lowerPrompt.contains("landing page") || lowerPrompt.contains("app")) {
            PlannedTaskDto deployTask = createBaseTask("task-deployment", "Docker & Cloud Deployment Infrastructure", "Containerize services and deploy cloud infrastructure", "DEPLOYMENT", 7, List.of("task-testing"));
            tasks.add(deployTask);
        }

        return tasks;
    }

    private PlannedTaskDto createBaseTask(String id, String name, String desc, String defaultCap, int priority, List<String> deps) {
        PlannedTaskDto task = new PlannedTaskDto();
        task.setTaskId(id);
        task.setTaskName(name);
        task.setDescription(desc);
        task.setTaskType(name);

        String cap = capabilityResolver.resolveCapability(name, desc);
        if ("GENERAL_COMPUTATION".equals(cap)) {
            cap = defaultCap;
        }
        task.setRequiredCapability(cap);
        task.setPriority(priority);
        task.setDependencies(new ArrayList<>(deps));
        task.setValidationRules(List.of("SCHEMA_CHECK", "NON_NULL_OUTPUT"));
        task.setStatus("PLANNED");

        estimator.estimateTask(task);
        return task;
    }
}
