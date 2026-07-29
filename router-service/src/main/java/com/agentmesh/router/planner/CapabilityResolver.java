package com.agentmesh.router.planner;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CapabilityResolver {

    public String resolveCapability(String taskType, String description) {
        String combined = ((taskType != null ? taskType : "") + " " + (description != null ? description : "")).toUpperCase(Locale.ROOT);

        if (combined.contains("RESEARCH") || combined.contains("MARKET")) {
            return "RESEARCH";
        }
        if (combined.contains("LOGO") || combined.contains("IMAGE") || combined.contains("BRAND") || combined.contains("GRAPHIC")) {
            return "IMAGE_GENERATION";
        }
        if (combined.contains("PITCH") || combined.contains("PRESENTATION") || combined.contains("DECK") || combined.contains("SLIDE")) {
            return "PRESENTATION_GENERATION";
        }
        if (combined.contains("AUTH") || combined.contains("AUTHENTICATION") || combined.contains("LOGIN") || combined.contains("JWT")) {
            return "AUTHENTICATION";
        }
        if (combined.contains("TEST") || combined.contains("QA") || combined.contains("AUDIT") || combined.contains("VULNERABILITY")) {
            return "TESTING";
        }
        if (combined.contains("DATABASE") || combined.contains("SCHEMA") || combined.contains("SQL") || combined.contains("DB")) {
            return "DATABASE_DESIGN";
        }
        if (combined.contains("DEPLOY") || combined.contains("DOCKER") || combined.contains("CLOUD") || combined.contains("KUBERNETES")) {
            return "DEPLOYMENT";
        }
        if (combined.contains("FRONTEND") || combined.matches(".*\\bUI\\b.*") || combined.contains("REACT") || combined.contains("LANDING PAGE")) {
            return "FRONTEND_DEVELOPMENT";
        }
        if (combined.contains("BACKEND") || combined.contains("API") || combined.contains("MICROSERVICE") || combined.contains("SPRING")) {
            return "BACKEND_DEVELOPMENT";
        }

        return "GENERAL_COMPUTATION";
    }
}
