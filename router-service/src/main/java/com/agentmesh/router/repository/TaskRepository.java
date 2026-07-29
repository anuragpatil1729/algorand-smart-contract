package com.agentmesh.router.repository;

import com.agentmesh.router.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByWorkflowId(String workflowId);
    List<Task> findByWorkflowIdAndStatus(String workflowId, String status);
}
