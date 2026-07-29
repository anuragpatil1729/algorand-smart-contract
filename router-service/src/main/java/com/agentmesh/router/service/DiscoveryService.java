package com.agentmesh.router.service;

import com.agentmesh.router.model.Agent;
import com.agentmesh.router.model.Quote;
import com.agentmesh.router.model.Task;

import java.util.List;

public interface DiscoveryService {
    List<Agent> discoverCapableAgents(Task task);
    List<Quote> collectAndScoreQuotes(Task task);
}
