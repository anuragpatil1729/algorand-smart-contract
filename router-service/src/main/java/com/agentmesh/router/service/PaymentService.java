package com.agentmesh.router.service;

import com.agentmesh.router.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse lockFunds(String workflowId);
    PaymentResponse releaseDisbursement(String workflowId);
    PaymentResponse refundEscrow(String workflowId);
    PaymentResponse getPaymentByWorkflowId(String workflowId);
    List<PaymentResponse> getAllPayments();
}
