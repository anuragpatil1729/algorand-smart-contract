package com.agentmesh.router.dto.mapper;

import com.agentmesh.router.dto.PaymentResponse;
import com.agentmesh.router.model.Payment;

public class PaymentMapper {

    public static PaymentResponse toDto(Payment payment) {
        if (payment == null) return null;
        return new PaymentResponse(
                payment.getId(),
                payment.getWorkflow() != null ? payment.getWorkflow().getId() : null,
                payment.getEscrowAddress(),
                payment.getTotalAmount(),
                payment.getPaymentStatus(),
                payment.getTransactionHash(),
                payment.getTxGroupId(),
                payment.getCreatedAt(),
                payment.getCompletedAt()
        );
    }
}
