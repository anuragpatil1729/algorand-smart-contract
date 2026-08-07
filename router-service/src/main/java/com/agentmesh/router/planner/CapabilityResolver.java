package com.agentmesh.router.planner;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CapabilityResolver {

    public String resolveCapability(String taskType, String description) {
        String combined = ((taskType != null ? taskType : "") + " " + (description != null ? description : "")).toUpperCase(Locale.ROOT);

        if (combined.contains("RESEARCH") || combined.contains("MARKET") || combined.contains("INTELLIGENCE")) {
            return "research";
        }
        if (combined.contains("TRANSLAT") || combined.contains("LANGUAG") || combined.contains("LOCALE") || combined.contains("INTERNATION")) {
            return "translation";
        }
        if (combined.contains("VISION") || combined.contains("LOGO") || combined.contains("IMAGE") || combined.contains("BRAND") || combined.contains("GRAPHIC")) {
            return "vision";
        }
        if (combined.contains("DEPLOY") || combined.contains("DOCKER") || combined.contains("CLOUD") || combined.contains("KUBERNETES") || combined.contains("DEVOPS")) {
            return "deployment";
        }
        if (combined.contains("PITCH") || combined.contains("PRESENTATION") || combined.contains("DECK") || combined.contains("SLIDE") || combined.contains("DOCUMENT") || combined.contains("README")) {
            return "documentation";
        }
        if (combined.contains("DATABASE") || combined.contains("SCHEMA") || combined.contains("SQL") || combined.contains("POSTGRES")) {
            return "database";
        }
        if (combined.contains("TEST") || combined.contains("QA") || combined.contains("AUDIT") || combined.contains("VULNERABILITY") || combined.contains("CHECK")) {
            return "testing";
        }
        if (combined.contains("ARCHITECT") || combined.contains("SYSTEM") || combined.contains("STRUCTURE")) {
            return "architecture";
        }
        if (combined.contains("FRONTEND") || combined.contains("BACKEND") || combined.contains("CODE") || combined.contains("DEV") || combined.contains("REACT") || combined.contains("API") || combined.contains("DESIGN")) {
            return "code-generation";
        }

        return "code-generation";
    }
}
