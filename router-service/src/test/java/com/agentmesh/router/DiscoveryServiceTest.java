package com.agentmesh.router;

import com.agentmesh.router.discovery.AgentDiscoveryService;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.model.enums.HealthStatus;
import com.agentmesh.router.planner.scoring.ScoringEngine;
import com.agentmesh.router.repository.AgentRepository;
import com.agentmesh.router.repository.QuoteRepository;
import com.agentmesh.router.repository.ScoringConfigRepository;
import com.agentmesh.router.service.RankingEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiscoveryServiceTest {

    private AgentRepository agentRepository;
    private AgentDiscoveryService discoveryService;

    @BeforeEach
    void setUp() {
        agentRepository = mock(AgentRepository.class);
        QuoteRepository quoteRepository = mock(QuoteRepository.class);
        ScoringConfigRepository scoringConfigRepository = mock(ScoringConfigRepository.class);
        ScoringEngine scoringEngine = new ScoringEngine();
        RankingEngine rankingEngine = new RankingEngine();
        RestTemplate restTemplate = new RestTemplate();

        discoveryService = new AgentDiscoveryService(
                agentRepository, quoteRepository, scoringConfigRepository, scoringEngine, rankingEngine, restTemplate
        );
    }

    @Test
    void testFindAgentsByCapability() {
        Agent logoAgent = Agent.builder()
                .id("a1")
                .name("Logo Agent")
                .capabilities("LOGO_DESIGN,SVG_GENERATION")
                .healthStatus(HealthStatus.ONLINE)
                .rating(4.9)
                .build();

        Agent codeAgent = Agent.builder()
                .id("a2")
                .name("Code Agent")
                .capabilities("CODING,REACT,SPRING_BOOT")
                .healthStatus(HealthStatus.ONLINE)
                .rating(4.8)
                .build();

        when(agentRepository.findAll()).thenReturn(List.of(logoAgent, codeAgent));

        List<Agent> results = discoveryService.findAgentsByCapability("LOGO_DESIGN");

        assertEquals(1, results.size());
        assertEquals("a1", results.get(0).getId());
    }

    @Test
    void testFindCheapestAgents() {
        Agent cheapAgent = Agent.builder().id("c1").basePrice(10.0).build();
        Agent expensiveAgent = Agent.builder().id("c2").basePrice(100.0).build();

        when(agentRepository.findAll()).thenReturn(List.of(expensiveAgent, cheapAgent));

        List<Agent> results = discoveryService.findCheapestAgents();

        assertEquals(2, results.size());
        assertEquals("c1", results.get(0).getId());
    }
}
