package com.agentmesh.router.x402.service;

import com.agentmesh.router.x402.dto.X402Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class X402AuditService {

    private static final Logger log = LoggerFactory.getLogger(X402AuditService.class);

    private final List<Map<String, Object>> auditLog = new CopyOnWriteArrayList<>();
    private final Map<String, X402Receipt> receiptsByWorkflow = new ConcurrentHashMap<>();
    private final Map<String, X402Receipt> receiptsByTransaction = new ConcurrentHashMap<>();

    public void audit(String action, String workflowId, String transactionId, String details) {
        Map<String, Object> record = new LinkedHashMap<>();
        record.put("auditId", "aud-" + UUID.randomUUID().toString().substring(0, 8));
        record.put("action", action);
        record.put("workflowId", workflowId);
        record.put("transactionId", transactionId);
        record.put("details", details);
        record.put("timestamp", System.currentTimeMillis());

        auditLog.add(record);
        log.info("[x402 Audit] {} | wf: {} | tx: {} | {}", action, workflowId, transactionId, details);
    }

    public void storeReceipt(X402Receipt receipt) {
        if (receipt == null) return;
        if (receipt.getWorkflowId() != null) {
            receiptsByWorkflow.put(receipt.getWorkflowId(), receipt);
        }
        if (receipt.getAlgorandTransactionId() != null) {
            receiptsByTransaction.put(receipt.getAlgorandTransactionId(), receipt);
        }
        audit("RECEIPT_GENERATED", receipt.getWorkflowId(), receipt.getAlgorandTransactionId(), "Receipt hash: " + receipt.getReceiptHash());
    }

    public X402Receipt getReceiptByWorkflow(String workflowId) {
        return receiptsByWorkflow.get(workflowId);
    }

    public X402Receipt getReceiptByTransaction(String transactionId) {
        return receiptsByTransaction.get(transactionId);
    }

    public List<X402Receipt> getAllReceipts() {
        return new ArrayList<>(receiptsByWorkflow.values());
    }

    public List<Map<String, Object>> getAuditLog() {
        return auditLog;
    }
}
