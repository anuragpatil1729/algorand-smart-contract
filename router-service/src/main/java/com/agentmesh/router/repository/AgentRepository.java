package com.agentmesh.router.repository;

import com.agentmesh.router.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {
    List<Agent> findByHealthStatus(String healthStatus);
}
