package com.agentmesh.router.repository;

import com.agentmesh.router.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    @Query("SELECT p FROM Payment p WHERE p.workflow.id = :workflowId")
    Optional<Payment> findByWorkflowId(@Param("workflowId") String workflowId);
}
