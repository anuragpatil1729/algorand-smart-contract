package com.agentmesh.router.repository;

import com.agentmesh.router.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, String> {
    List<Quote> findByTaskId(String taskId);
    List<Quote> findByTaskIdAndSelected(String taskId, Boolean selected);
}
