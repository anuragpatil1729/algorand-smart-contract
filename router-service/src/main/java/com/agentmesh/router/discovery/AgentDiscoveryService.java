package com.agentmesh.router.discovery;

import com.agentmesh.router.dto.QuoteRequestDto;
import com.agentmesh.router.dto.QuoteResponseDto;
import com.agentmesh.router.model.Agent;
import com.agentmesh.router.model.Quote;
import com.agentmesh.router.model.ScoringConfig;
import com.agentmesh.router.model.Task;
import com.agentmesh.router.model.enums.HealthStatus;
import com.agentmesh.router.planner.scoring.ScoringEngine;
import com.agentmesh.router.repository.AgentRepository;
import com.agentmesh.router.repository.QuoteRepository;
import com.agentmesh.router.repository.ScoringConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AgentDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(AgentDiscoveryService.class);

    private final AgentRepository agentRepository;
    private final QuoteRepository quoteRepository;
    private final ScoringConfigRepository scoringConfigRepository;
    private final ScoringEngine scoringEngine;
    private final RestTemplate restTemplate;

    public AgentDiscoveryService(AgentRepository agentRepository, QuoteRepository quoteRepository, ScoringConfigRepository scoringConfigRepository, ScoringEngine scoringEngine, RestTemplate restTemplate) {
        this.agentRepository = agentRepository;
        this.quoteRepository = quoteRepository;
        this.scoringConfigRepository = scoringConfigRepository;
        this.scoringEngine = scoringEngine;
        this.restTemplate = restTemplate;
    }

    public List<Agent> discoverAllAgents() {
        return agentRepository.findAll();
    }

    public List<Quote> collectAndScoreQuotesForTask(Task task) {
        List<Agent> agents = agentRepository.findAll();
        if (agents.isEmpty()) {
            initDefaultAgents();
            agents = agentRepository.findAll();
        }

        List<Quote> rawQuotes = new ArrayList<>();
        ScoringConfig scoringConfig = scoringConfigRepository.findById("DEFAULT")
                .orElse(ScoringConfig.builder().build());

        for (Agent agent : agents) {
            if (agentMatchesTask(agent, task.getTaskType())) {
                Quote quote = requestQuoteFromAgent(agent, task);
                rawQuotes.add(quote);
            }
        }

        for (Quote quote : rawQuotes) {
            double score = scoringEngine.calculateScore(quote, rawQuotes, scoringConfig);
            quote.setScore(score);
        }

        rawQuotes.sort(Comparator.comparingDouble(Quote::getScore).reversed());
        if (!rawQuotes.isEmpty()) {
            rawQuotes.get(0).setSelected(true);
            task.setAssignedAgent(rawQuotes.get(0).getAgent());
            task.setPrice(rawQuotes.get(0).getPrice());
        }

        return quoteRepository.saveAll(rawQuotes);
    }

    private Quote requestQuoteFromAgent(Agent agent, Task task) {
        String quoteUrl = agent.getEndpoint() + "/quote";
        QuoteRequestDto request = new QuoteRequestDto(task.getId(), task.getTaskType(), task.getDescription(), task.getEstimatedComplexity());

        try {
            QuoteResponseDto res = restTemplate.postForObject(quoteUrl, request, QuoteResponseDto.class);
            if (res != null) {
                return Quote.builder()
                        .id(UUID.randomUUID().toString())
                        .workflow(task.getWorkflow())
                        .taskId(task.getId())
                        .agent(agent)
                        .quotedPrice(res.getPrice())
                        .estimatedTime(res.getEstimatedTime())
                        .confidence(res.getConfidence())
                        .reputationScore(res.getRating() != null ? res.getRating() : 4.5)
                        .score(0.0)
                        .selected(false)
                        .build();
            }
        } catch (Exception e) {
            log.warn("Microservice at {} unreachable, creating dynamic quote fallback for {}", agent.getEndpoint(), agent.getName());
        }

        double complexityFactor = "HIGH".equals(task.getEstimatedComplexity()) ? 1.4 : ("LOW".equals(task.getEstimatedComplexity()) ? 0.8 : 1.0);
        double price = Math.round((agent.getBasePrice() * complexityFactor + (Math.random() * 5.0)) * 100.0) / 100.0;
        int eta = (int) (10 * complexityFactor + (Math.random() * 4));

        return Quote.builder()
                .id(UUID.randomUUID().toString())
                .workflow(task.getWorkflow())
                .taskId(task.getId())
                .agent(agent)
                .quotedPrice(price)
                .estimatedTime(eta)
                .confidence(Math.round((94.0 + Math.random() * 5.0) * 10.0) / 10.0)
                .reputationScore(agent.getRating())
                .score(0.0)
                .selected(false)
                .build();
    }

    private boolean agentMatchesTask(Agent agent, String taskType) {
        if (agent == null || agent.getCapabilities() == null || taskType == null) return false;
        String caps = agent.getCapabilities().toUpperCase();
        String type = taskType.toUpperCase();
        if (type.contains("RESEARCH") && caps.contains("RESEARCH")) return true;
        if ((type.contains("FRONTEND") || type.contains("BACKEND") || type.contains("CODE")) && caps.contains("DEVELOPMENT")) return true;
        if ((type.contains("LOGO") || type.contains("DESIGN") || type.contains("GRAPHICS")) && caps.contains("LOGO_DESIGN")) return true;
        if ((type.contains("PITCH") || type.contains("PRESENTATION") || type.contains("DECK")) && caps.contains("PRESENTATION")) return true;
        if ((type.contains("TESTING") || type.contains("QA") || type.contains("AUDIT")) && caps.contains("TESTING")) return true;
        return caps.contains(type);
    }

    public void initDefaultAgents() {
        if (agentRepository.count() == 0) {
            List<Agent> defaultAgents = List.of(
                    Agent.builder()
                            .id("agent-research-01")
                            .name("Research & Market Intelligence Agent")
                            .endpoint("http://localhost:8001")
                            .walletAddress("D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ")
                            .rating(4.9)
                            .successRate(98.5)
                            .healthStatus(HealthStatus.UP)
                            .basePrice(45.0)
                            .capabilities("RESEARCH,MARKET_ANALYSIS,COMPETITOR_RESEARCH,SUMMARY")
                            .build(),
                    Agent.builder()
                            .id("agent-code-02")
                            .name("Full-Stack Coding & Architecture Agent")
                            .endpoint("http://localhost:8002")
                            .walletAddress("XU4URLGPIYXCXPXYHBTHGLWPLEZOP2F3D7OM2VSRTWK4QEKTKRF6T74KJI")
                            .rating(4.8)
                            .successRate(96.0)
                            .healthStatus(HealthStatus.UP)
                            .basePrice(80.0)
                            .capabilities("FRONTEND,BACKEND,DEVELOPMENT,REACT,SPRING_BOOT,API_DESIGN")
                            .build(),
                    Agent.builder()
                            .id("agent-image-03")
                            .name("Brand & Visual Graphics Agent")
                            .endpoint("http://localhost:8003")
                            .walletAddress("KVYGHYDZ4GGDUD4KZ555XRUGG7GHBJQT3FWCNHE47E2PCDSUY54XOIHZ2U")
                            .rating(4.95)
                            .successRate(99.0)
                            .healthStatus(HealthStatus.UP)
                            .basePrice(60.0)
                            .capabilities("LOGO_DESIGN,BRANDING,UI_UX,GRAPHICS,SVG_GENERATION")
                            .build(),
                    Agent.builder()
                            .id("agent-ppt-04")
                            .name("Pitch Deck & Strategy Agent")
                            .endpoint("http://localhost:8004")
                            .walletAddress("5BJXBTQPXI6MAPHJF2YHTPABUAEM5ZDGEZWSBN5OQXQWQ67HVW47OUIUOU")
                            .rating(4.75)
                            .successRate(94.5)
                            .healthStatus(HealthStatus.UP)
                            .basePrice(55.0)
                            .capabilities("PRESENTATION,PITCH_DECK,BUSINESS_PLAN,SLIDE_GENERATION")
                            .build(),
                    Agent.builder()
                            .id("agent-testing-05")
                            .name("Automated QA & Security Agent")
                            .endpoint("http://localhost:8005")
                            .walletAddress("MB3R5YONVGOARERGS2O2FAQ5MXRIZOKPFCGALD5DP7BJWFSKO3ZDUBLNRQ")
                            .rating(4.85)
                            .successRate(97.2)
                            .healthStatus(HealthStatus.UP)
                            .basePrice(35.0)
                            .capabilities("TESTING,QA,CODE_AUDIT,SECURITY_CHECK,VALIDATION")
                            .build()
            );
            agentRepository.saveAll(defaultAgents);
        }
    }
}
