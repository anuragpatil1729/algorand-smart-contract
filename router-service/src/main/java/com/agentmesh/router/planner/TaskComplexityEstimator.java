package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import org.springframework.stereotype.Component;

@Component
public class TaskComplexityEstimator {

    public void estimateTask(PlannedTaskDto task) {
        if (task == null) return;
        String capability = task.getRequiredCapability() != null ? task.getRequiredCapability() : "GENERAL";

        switch (capability) {
            case "RESEARCH":
                task.setComplexity("LOW");
                task.setEstimatedDurationSeconds(12);
                task.setEstimatedCost(45.0);
                break;
            case "IMAGE_GENERATION":
                task.setComplexity("MEDIUM");
                task.setEstimatedDurationSeconds(10);
                task.setEstimatedCost(60.0);
                break;
            case "PRESENTATION_GENERATION":
                task.setComplexity("MEDIUM");
                task.setEstimatedDurationSeconds(11);
                task.setEstimatedCost(55.0);
                break;
            case "FRONTEND_DEVELOPMENT":
                task.setComplexity("HIGH");
                task.setEstimatedDurationSeconds(18);
                task.setEstimatedCost(80.0);
                break;
            case "BACKEND_DEVELOPMENT":
                task.setComplexity("HIGH");
                task.setEstimatedDurationSeconds(20);
                task.setEstimatedCost(85.0);
                break;
            case "DATABASE_DESIGN":
                task.setComplexity("MEDIUM");
                task.setEstimatedDurationSeconds(15);
                task.setEstimatedCost(70.0);
                break;
            case "AUTHENTICATION":
                task.setComplexity("MEDIUM");
                task.setEstimatedDurationSeconds(14);
                task.setEstimatedCost(65.0);
                break;
            case "TESTING":
                task.setComplexity("LOW");
                task.setEstimatedDurationSeconds(8);
                task.setEstimatedCost(35.0);
                break;
            case "DEPLOYMENT":
                task.setComplexity("HIGH");
                task.setEstimatedDurationSeconds(25);
                task.setEstimatedCost(90.0);
                break;
            default:
                task.setComplexity("MEDIUM");
                task.setEstimatedDurationSeconds(15);
                task.setEstimatedCost(50.0);
                break;
        }
    }
}
