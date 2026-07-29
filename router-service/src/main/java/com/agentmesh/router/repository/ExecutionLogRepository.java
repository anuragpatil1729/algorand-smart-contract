package com.agentmesh.router.repository;

import com.agentmesh.router.model.ExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, String> {
    List<ExecutionLog> findByWorkflowIdOrderByTimestampDesc(String workflowId);
    List<ExecutionLog> findTop100ByOrderByTimestampDesc();
}
