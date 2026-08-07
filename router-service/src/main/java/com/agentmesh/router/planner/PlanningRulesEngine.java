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

        // 2. Security Red-Teaming & Social Engineering Rule
        boolean isSecurityPrompt = lowerPrompt.contains("security") || lowerPrompt.contains("override") || 
                                   lowerPrompt.contains("risk") || lowerPrompt.contains("flag") || 
                                   lowerPrompt.contains("emergency") || lowerPrompt.contains("card") || 
                                   lowerPrompt.contains("jailbreak") || lowerPrompt.contains("character") || 
                                   lowerPrompt.contains("prompt") || lowerPrompt.contains("attack");
        if (isSecurityPrompt) {
            PlannedTaskDto secTask1 = createBaseTask("task-sec-strategy", "Social Engineering & Threat Vector Analysis", "Identify attack vectors, social engineering prompts, and context manipulation", "documentation", 2, List.of("task-research"));
            tasks.add(secTask1);

            PlannedTaskDto secTask2 = createBaseTask("task-sec-guardrail", "Guardrail & System Prompt Enforcement", "Verify override protection, safety boundaries, and policy compliance", "code-generation", 3, List.of("task-sec-strategy"));
            tasks.add(secTask2);

            PlannedTaskDto secTask3 = createBaseTask("task-sec-state", "Account & State Isolation Checking", "Check sensitive account override parameters and state isolation", "database", 4, List.of("task-sec-guardrail"));
            tasks.add(secTask3);
        }

        // 3. Presentation / Pitch Deck Rule
        if (lowerPrompt.contains("pitch") || lowerPrompt.contains("deck") || lowerPrompt.contains("slide") || lowerPrompt.contains("presentation")) {
            PlannedTaskDto pptTask = createBaseTask("task-presentation", "Pitch Deck Strategy & Slide Generation", "Generate executive pitch deck slides", "documentation", 2, List.of("task-research"));
            tasks.add(pptTask);
        }

        // 4. Logo / Branding / Visual Design Rule
        if (lowerPrompt.contains("logo") || lowerPrompt.contains("brand") || lowerPrompt.contains("graphic") || lowerPrompt.contains("landing page") || lowerPrompt.contains("startup") || lowerPrompt.contains("website")) {
            PlannedTaskDto logoTask = createBaseTask("task-logo", "Brand Identity & Vector Graphics", "Create vector SVG logos and brand color guidelines", "vision", 2, List.of("task-research"));
            tasks.add(logoTask);
        }

        // 5. Web / Frontend UI Rule
        boolean hasFrontend = lowerPrompt.contains("landing page") || lowerPrompt.contains("website") || lowerPrompt.contains("frontend") || lowerPrompt.contains("ui") || lowerPrompt.contains("app") || lowerPrompt.contains("dashboard");
        if (hasFrontend) {
            List<String> feDeps = new ArrayList<>();
            feDeps.add("task-research");
            if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-logo"))) {
                feDeps.add("task-logo");
            }
            PlannedTaskDto feTask = createBaseTask("task-frontend", "Frontend UI Component", "Build React UI components and landing page", "code-generation", 3, feDeps);
            tasks.add(feTask);
        }

        // 6. Backend API Rule
        boolean hasBackend = lowerPrompt.contains("backend") || lowerPrompt.contains("api") || lowerPrompt.contains("service") || lowerPrompt.contains("app") || lowerPrompt.contains("landing page") || lowerPrompt.contains("authentication") || lowerPrompt.contains("database") || lowerPrompt.contains("fastapi");
        if (hasBackend) {
            PlannedTaskDto beTask = createBaseTask("task-backend", "Backend REST API Microservices", "Implement REST API controllers and service logic", "code-generation", 3, List.of("task-research"));
            tasks.add(beTask);
        }

        // 7. Database Schema Rule
        if (lowerPrompt.contains("database") || lowerPrompt.contains("db") || lowerPrompt.contains("schema") || lowerPrompt.contains("sql") || lowerPrompt.contains("app") || lowerPrompt.contains("account") || lowerPrompt.contains("card")) {
            List<String> dbDeps = new ArrayList<>();
            if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-backend"))) {
                dbDeps.add("task-backend");
            } else {
                dbDeps.add("task-research");
            }
            PlannedTaskDto dbTask = createBaseTask("task-database", "Database Schema & Entity Models", "Design relational database schema and JPA entities", "database", 4, dbDeps);
            tasks.add(dbTask);
        }

        // 8. Authentication Rule
        if (lowerPrompt.contains("authentication") || lowerPrompt.contains("auth") || lowerPrompt.contains("login") || lowerPrompt.contains("jwt")) {
            List<String> authDeps = new ArrayList<>();
            if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-backend"))) {
                authDeps.add("task-backend");
            }
            if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-database"))) {
                authDeps.add("task-database");
            }
            if (authDeps.isEmpty()) authDeps.add("task-research");

            PlannedTaskDto authTask = createBaseTask("task-auth", "Authentication & Authorization Security", "Implement JWT auth and security filter chains", "code-generation", 5, authDeps);
            tasks.add(authTask);
        }

        // 9. Testing & QA Audit Rule
        List<String> testingDeps = new ArrayList<>();
        if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-frontend"))) testingDeps.add("task-frontend");
        if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-auth"))) testingDeps.add("task-auth");
        if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-sec-state"))) testingDeps.add("task-sec-state");
        if (tasks.stream().anyMatch(t -> t.getTaskId().equals("task-backend")) && testingDeps.isEmpty()) testingDeps.add("task-backend");
        if (testingDeps.isEmpty()) testingDeps.add("task-research");

        PlannedTaskDto qaTask = createBaseTask("task-testing", "Automated QA & Security Audit", "Execute test suites and security vulnerability audit", "testing", 6, testingDeps);
        tasks.add(qaTask);

        // 10. Deployment Rule
        if (lowerPrompt.contains("deploy") || lowerPrompt.contains("deployment") || lowerPrompt.contains("docker") || lowerPrompt.contains("cloud") || lowerPrompt.contains("landing page") || lowerPrompt.contains("app")) {
            PlannedTaskDto deployTask = createBaseTask("task-deployment", "Docker & Cloud Deployment Infrastructure", "Containerize services and deploy cloud infrastructure", "deployment", 7, List.of("task-testing"));
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
