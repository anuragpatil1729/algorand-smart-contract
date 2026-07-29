package com.agentmesh.router.payment;

import com.agentmesh.router.model.Agent;
import com.agentmesh.router.model.Payment;
import com.agentmesh.router.model.Task;
import com.agentmesh.router.model.Transaction;
import com.agentmesh.router.model.Workflow;
import com.agentmesh.router.model.enums.PaymentStatus;
import com.agentmesh.router.repository.AgentRepository;
import com.agentmesh.router.repository.PaymentRepository;
import com.agentmesh.router.repository.TransactionRepository;
import com.agentmesh.router.repository.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AlgorandPaymentService {

    private static final Logger log = LoggerFactory.getLogger(AlgorandPaymentService.class);

    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final WorkflowRepository workflowRepository;
    private final AgentRepository agentRepository;

    private static final String ESCROW_WALLET = "6SA3SIOK5ZE3VU3K3CJXUOOOQ2NKDHVPLOUCO5KIGZPS32JL7SGA6ZAY6Y";
    private static final String USER_WALLET = "USERALGORANDWALLET3M9Q8P7X6V5C4B3N2M1K9J8H7G6F5D4S3A8B7C";
    private static final String ROUTER_FEE_POOL = "P7FGL63UC3QF2EHU76XTE4F64LJA6LXJGWZHOG6XYQLSCCO3RUTKSQPQNQ";

    public AlgorandPaymentService(PaymentRepository paymentRepository, TransactionRepository transactionRepository, WorkflowRepository workflowRepository, AgentRepository agentRepository) {
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.workflowRepository = workflowRepository;
        this.agentRepository = agentRepository;
    }

    @Transactional
    public Payment lockFundsInEscrow(Workflow workflow) {
        String paymentId = "pay-" + UUID.randomUUID().toString().substring(0, 8);
        Payment payment = Payment.builder()
                .id(paymentId)
                .workflow(workflow)
                .escrowAddress(ESCROW_WALLET)
                .totalAmount(workflow.getTotalPrice())
                .paymentStatus(PaymentStatus.HELD_IN_ESCROW)
                .txGroupId("ALG-GROUP-INIT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        workflow.setEscrowAddress(ESCROW_WALLET);
        workflow.setEscrowStatus("LOCKED");
        workflowRepository.save(workflow);

        Transaction depositTx = Transaction.builder()
                .id("tx-" + UUID.randomUUID().toString().substring(0, 8))
                .paymentId(paymentId)
                .txHash("ALG-TX-DEP-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .senderWallet(USER_WALLET)
                .receiverWallet(ESCROW_WALLET)
                .amount(workflow.getTotalPrice())
                .status("SUCCESS")
                .blockRound(34589210L + (long)(Math.random() * 500))
                .timestamp(LocalDateTime.now())
                .build();

        transactionRepository.save(depositTx);
        log.info("Locked {} Algos in Algorand Escrow for workflow {}", workflow.getTotalPrice(), workflow.getId());
        return payment;
    }

    @Transactional
    public Payment releaseAtomicPayment(Workflow workflow, List<Task> completedTasks) {
        Payment payment = paymentRepository.findByWorkflowId(workflow.getId())
                .orElseGet(() -> lockFundsInEscrow(workflow));

        String groupId = "ALG-GROUP-ATOMIC-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        payment.setTxGroupId(groupId);
        payment.setPaymentStatus(PaymentStatus.DISBURSED);
        payment.setCompletedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        long blockRound = 34589250L + (long)(Math.random() * 500);

        double protocolFee = roundAmount(workflow.getTotalPrice() * 0.015);
        Transaction feeTx = Transaction.builder()
                .id("tx-" + UUID.randomUUID().toString().substring(0, 8))
                .paymentId(payment.getId())
                .txHash("ALG-TX-FEE-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .senderWallet(ESCROW_WALLET)
                .receiverWallet(ROUTER_FEE_POOL)
                .amount(protocolFee)
                .status("SUCCESS")
                .blockRound(blockRound)
                .timestamp(LocalDateTime.now())
                .build();
        transactionRepository.save(feeTx);

        for (Task task : completedTasks) {
            if ("COMPLETED".equalsIgnoreCase(task.getStatus()) && task.getAssignedAgent() != null) {
                Agent agent = task.getAssignedAgent();
                String agentWallet = (agent != null && agent.getWalletAddress() != null) ? agent.getWalletAddress() : "D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ";

                Transaction payoutTx = Transaction.builder()
                        .id("tx-" + UUID.randomUUID().toString().substring(0, 8))
                        .paymentId(payment.getId())
                        .txHash("ALG-TX-PAY-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                        .senderWallet(ESCROW_WALLET)
                        .receiverWallet(agentWallet)
                        .amount(roundAmount(task.getPrice()))
                        .agentId(agent != null ? agent.getId() : "N/A")
                        .status("SUCCESS")
                        .blockRound(blockRound)
                        .timestamp(LocalDateTime.now())
                        .build();
                transactionRepository.save(payoutTx);
            }
        }

        workflow.setEscrowStatus("RELEASED");
        workflowRepository.save(workflow);

        log.info("Executed Algorand Atomic Group Transfer payment {} for workflow {}", groupId, workflow.getId());
        return payment;
    }

    @Transactional
    public Payment refundUser(Workflow workflow) {
        Payment payment = paymentRepository.findByWorkflowId(workflow.getId()).orElse(null);
        if (payment != null) {
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
            payment.setCompletedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            Transaction refundTx = Transaction.builder()
                    .id("tx-" + UUID.randomUUID().toString().substring(0, 8))
                    .paymentId(payment.getId())
                    .txHash("ALG-TX-REFUND-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                    .senderWallet(ESCROW_WALLET)
                    .receiverWallet(USER_WALLET)
                    .amount(workflow.getTotalPrice())
                    .status("SUCCESS")
                    .blockRound(34589300L + (long)(Math.random() * 500))
                    .timestamp(LocalDateTime.now())
                    .build();
            transactionRepository.save(refundTx);

            workflow.setEscrowStatus("REFUNDED");
            workflowRepository.save(workflow);
        }
        return payment;
    }

    private double roundAmount(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
