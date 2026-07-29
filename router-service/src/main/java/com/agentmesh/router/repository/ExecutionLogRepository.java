package com.agentmesh.router.repository;

import com.agentmesh.router.model.ExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, String> {
    @Query("SELECT e FROM ExecutionLog e WHERE e.workflow.id = :workflowId")
    List<ExecutionLog> findByWorkflowId(@Param("workflowId") String workflowId);

    @Query("SELECT e FROM ExecutionLog e WHERE e.workflow.id = :workflowId ORDER BY e.timestamp DESC")
    List<ExecutionLog> findByWorkflowIdOrderByTimestampDesc(@Param("workflowId") String workflowId);

    @Query("SELECT e FROM ExecutionLog e ORDER BY e.timestamp DESC LIMIT 100")
    List<ExecutionLog> findTop100ByOrderByTimestampDesc();
}
