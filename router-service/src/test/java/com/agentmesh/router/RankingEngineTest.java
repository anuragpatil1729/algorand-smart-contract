package com.agentmesh.router;

import com.agentmesh.router.model.Agent;
import com.agentmesh.router.service.RankingEngine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RankingEngineTest {

    @Test
    void testRankingEngineScoreCalculation() {
        RankingEngine engine = new RankingEngine();

        Agent topAgent = Agent.builder()
                .id("agent-top")
                .name("Top Agent")
                .rating(5.0)
                .healthScore(100.0)
                .averageResponseTime(20.0)
                .currentLoad(5.0)
                .basePrice(30.0)
                .build();

        Agent averageAgent = Agent.builder()
                .id("agent-avg")
                .name("Average Agent")
                .rating(3.5)
                .healthScore(70.0)
                .averageResponseTime(200.0)
                .currentLoad(60.0)
                .basePrice(80.0)
                .build();

        List<Agent> pool = List.of(topAgent, averageAgent);

        double topScore = engine.calculateScore(topAgent, pool);
        double avgScore = engine.calculateScore(averageAgent, pool);

        assertTrue(topScore > avgScore, "Top agent should score higher than average agent");
        assertTrue(topScore >= 80.0, "Top agent score should be >= 80");
    }
}
