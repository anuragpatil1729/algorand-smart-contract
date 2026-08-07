package com.agentmesh.router.planner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityResolverTest {

    private final CapabilityResolver resolver = new CapabilityResolver();

    @Test
    void testResolveCapabilities() {
        assertEquals("research", resolver.resolveCapability("Research Task", "Market research analysis"));
        assertEquals("vision", resolver.resolveCapability("Logo Design", "SVG branding logo"));
        assertEquals("documentation", resolver.resolveCapability("Pitch Deck", "Investor pitch deck slides"));
        assertEquals("code-generation", resolver.resolveCapability("React UI", "Landing page frontend UI"));
        assertEquals("code-generation", resolver.resolveCapability("REST API", "Spring Boot microservice API"));
        assertEquals("database", resolver.resolveCapability("Database Schema", "PostgreSQL relational schema"));
        assertEquals("testing", resolver.resolveCapability("QA Audit", "Security vulnerability testing"));
        assertEquals("deployment", resolver.resolveCapability("Cloud Deploy", "Docker container deployment"));
    }
}
