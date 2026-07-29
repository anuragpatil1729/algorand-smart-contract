package com.agentmesh.router.repository;

import com.agentmesh.router.model.AgentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentEventRepository extends JpaRepository<AgentEvent, String> {
    List<AgentEvent> findByAgentIdOrderByCreatedAtDesc(String agentId);
}
