package com.agentmesh.router.repository;

import com.agentmesh.router.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    @Query("SELECT t FROM Task t WHERE t.workflow.id = :workflowId")
    List<Task> findByWorkflowId(@Param("workflowId") String workflowId);
}
