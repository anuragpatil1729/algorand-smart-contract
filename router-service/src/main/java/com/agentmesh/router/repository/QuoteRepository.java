package com.agentmesh.router.repository;

import com.agentmesh.router.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, String> {
    @Query("SELECT q FROM Quote q WHERE q.workflow.id = :workflowId")
    List<Quote> findByWorkflowId(@Param("workflowId") String workflowId);

    @Query("SELECT q FROM Quote q WHERE q.taskId = :taskId")
    List<Quote> findByTaskId(@Param("taskId") String taskId);
}
