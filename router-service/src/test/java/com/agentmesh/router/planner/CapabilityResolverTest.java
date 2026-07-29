package com.agentmesh.router.planner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityResolverTest {

    private final CapabilityResolver resolver = new CapabilityResolver();

    @Test
    void testResolveCapabilities() {
        assertEquals("RESEARCH", resolver.resolveCapability("Research Task", "Market research analysis"));
        assertEquals("IMAGE_GENERATION", resolver.resolveCapability("Logo Design", "SVG branding logo"));
        assertEquals("PRESENTATION_GENERATION", resolver.resolveCapability("Pitch Deck", "Investor pitch deck slides"));
        assertEquals("FRONTEND_DEVELOPMENT", resolver.resolveCapability("React UI", "Landing page frontend UI"));
        assertEquals("BACKEND_DEVELOPMENT", resolver.resolveCapability("REST API", "Spring Boot microservice API"));
        assertEquals("DATABASE_DESIGN", resolver.resolveCapability("Database Schema", "PostgreSQL relational schema"));
        assertEquals("AUTHENTICATION", resolver.resolveCapability("Auth Service", "JWT authentication login"));
        assertEquals("TESTING", resolver.resolveCapability("QA Audit", "Security vulnerability testing"));
        assertEquals("DEPLOYMENT", resolver.resolveCapability("Cloud Deploy", "Docker container deployment"));
    }
}
