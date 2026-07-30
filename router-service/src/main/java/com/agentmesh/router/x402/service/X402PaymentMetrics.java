package com.agentmesh.router.x402.service;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;

@Component
public class X402PaymentMetrics {

    private final AtomicInteger paidRequestsCount = new AtomicInteger(0);
    private final AtomicInteger failedPaymentsCount = new AtomicInteger(0);
    private final AtomicInteger successfulTransactions = new AtomicInteger(0);
    private final AtomicInteger replayAttemptsBlocked = new AtomicInteger(0);
    private final DoubleAdder totalRevenueUsdc = new DoubleAdder();
    private final DoubleAdder totalSettlementTimeMs = new DoubleAdder();
    private final DoubleAdder totalVerificationTimeMs = new DoubleAdder();

    public void recordPaidRequest(double amountUsdc, long settlementTimeMs, long verificationTimeMs) {
        paidRequestsCount.incrementAndGet();
        successfulTransactions.incrementAndGet();
        totalRevenueUsdc.add(amountUsdc);
        totalSettlementTimeMs.add(settlementTimeMs);
        totalVerificationTimeMs.add(verificationTimeMs);
    }

    public void recordFailedPayment() {
        failedPaymentsCount.incrementAndGet();
    }

    public void recordReplayBlocked() {
        replayAttemptsBlocked.incrementAndGet();
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        int success = successfulTransactions.get();
        int failed = failedPaymentsCount.get();

        double avgSettlement = success > 0 ? (totalSettlementTimeMs.sum() / success) : 0.0;
        double avgVerification = success > 0 ? (totalVerificationTimeMs.sum() / success) : 0.0;

        metrics.put("paidRequestsCount", paidRequestsCount.get());
        metrics.put("successfulTransactions", success);
        metrics.put("failedPaymentsCount", failed);
        metrics.put("replayAttemptsBlocked", replayAttemptsBlocked.get());
        metrics.put("totalRevenueUSDC", Math.round(totalRevenueUsdc.sum() * 100.0) / 100.0);
        metrics.put("averageSettlementTimeMs", Math.round(avgSettlement * 10.0) / 10.0);
        metrics.put("averageVerificationTimeMs", Math.round(avgVerification * 10.0) / 10.0);

        return metrics;
    }
}
