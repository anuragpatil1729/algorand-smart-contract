package com.agentmesh.router.repository;

import com.agentmesh.router.model.ScoringConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoringConfigRepository extends JpaRepository<ScoringConfig, String> {
}
