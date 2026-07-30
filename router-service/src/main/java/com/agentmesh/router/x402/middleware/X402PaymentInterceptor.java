package com.agentmesh.router.x402.middleware;

import com.agentmesh.router.execution.dto.WorkflowExecutionRequest;
import com.agentmesh.router.x402.dto.X402Challenge;
import com.agentmesh.router.x402.dto.X402PaymentProof;
import com.agentmesh.router.x402.model.PaymentContext;
import com.agentmesh.router.x402.pricing.X402DynamicPricingEngine;
import com.agentmesh.router.x402.provider.AlgorandX402Provider;
import com.agentmesh.router.x402.service.X402AuditService;
import com.agentmesh.router.x402.service.X402PaymentMetrics;
import com.agentmesh.router.x402.service.X402SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.*;

@Component
public class X402PaymentInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(X402PaymentInterceptor.class);

    private final AlgorandX402Provider paymentProvider;
    private final X402DynamicPricingEngine pricingEngine;
    private final X402SecurityService securityService;
    private final X402AuditService auditService;
    private final X402PaymentMetrics metrics;
    private final ObjectMapper objectMapper;

    public X402PaymentInterceptor(
            AlgorandX402Provider paymentProvider,
            X402DynamicPricingEngine pricingEngine,
            X402SecurityService securityService,
            X402AuditService auditService,
            X402PaymentMetrics metrics
    ) {
        this.paymentProvider = paymentProvider;
        this.pricingEngine = pricingEngine;
        this.securityService = securityService;
        this.auditService = auditService;
        this.metrics = metrics;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // Only protect execution endpoints with x402 payment requirements
        if (!isPaidExecutionEndpoint(uri, method)) {
            return true;
        }

        log.info("Interception check on paid endpoint: {} {}", method, uri);

        String paymentProofHeader = extractPaymentProofHeader(request);

        // Scenario 1: Missing payment proof header -> Issue HTTP 402 Payment Required Challenge
        if (paymentProofHeader == null || paymentProofHeader.isBlank()) {
            issuePaymentRequiredChallenge(request, response, uri);
            return false;
        }

        // Scenario 2: Parse Payment Proof Payload
        long verifyStart = System.currentTimeMillis();
        X402PaymentProof proof = parsePaymentProof(paymentProofHeader);

        if (proof == null || proof.getTransactionId() == null) {
            log.warn("Malformed x402 payment proof header on request to {}", uri);
            metrics.recordFailedPayment();
            auditService.audit("VERIFICATION_FAILED", "unknown", null, "Malformed proof payload header");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            writeJsonResponse(response, Map.of("error", "Malformed x402 payment proof", "status", 400));
            return false;
        }

        // Check for replay attacks
        if (securityService.isReplayAttempt(proof.getTransactionId())) {
            log.warn("Replay attack attempt detected for transaction ID {}", proof.getTransactionId());
            metrics.recordReplayBlocked();
            metrics.recordFailedPayment();
            auditService.audit("REPLAY_ATTEMPT_BLOCKED", "unknown", proof.getTransactionId(), "Duplicate transaction ID reuse attempt");
            response.setStatus(HttpStatus.CONFLICT.value());
            writeJsonResponse(response, Map.of("error", "Duplicate transaction ID. Replay protection active.", "status", 409));
            return false;
        }

        // Verify challenge & transaction via Algorand Provider / x402 Facilitator
        X402Challenge challenge = securityService.getChallenge(proof.getChallengeId());
        boolean verified = paymentProvider.verifyPayment(proof, challenge);

        long verifyDuration = System.currentTimeMillis() - verifyStart;

        if (!verified) {
            log.warn("x402 Payment verification failed for transaction ID {}", proof.getTransactionId());
            metrics.recordFailedPayment();
            auditService.audit("VERIFICATION_FAILED", proof.getChallengeId(), proof.getTransactionId(), "Facilitator verification rejected payment");
            response.setStatus(HttpStatus.PAYMENT_REQUIRED.value());
            writeJsonResponse(response, Map.of("error", "x402 Payment verification failed", "status", 402));
            return false;
        }

        // Scenario 3: Verification Succeeded -> Register Tx, Set Context, and Generate Receipt
        securityService.registerProcessedTransaction(proof.getTransactionId());

        PaymentContext context = PaymentContext.getCurrent();
        context.setChallengeId(proof.getChallengeId());
        context.setTransactionId(proof.getTransactionId());
        context.setAmountPaid(proof.getAmount() != null ? proof.getAmount() : 5.0);
        context.setVerified(true);
        context.setPaymentStatus("SETTLED");
        context.setPaymentProof(proof);

        paymentProvider.generateReceipt(context);
        auditService.storeReceipt(context.getReceipt());

        metrics.recordPaidRequest(context.getAmountPaid(), 100L, verifyDuration);
        auditService.audit("PAYMENT_VERIFIED", context.getWorkflowId(), proof.getTransactionId(), "Paid request successfully verified and settled");

        log.info("x402 Payment verified successfully for transaction {}. Proceeding to workflow execution.", proof.getTransactionId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        PaymentContext.clear();
    }

    private boolean isPaidExecutionEndpoint(String uri, String method) {
        if (!"POST".equalsIgnoreCase(method)) return false;
        return uri.endsWith("/api/execution/start") || uri.endsWith("/api/workflows/execute") || uri.endsWith("/api/workflows/run");
    }

    private String extractPaymentProofHeader(HttpServletRequest request) {
        String proof = request.getHeader("X-402-Payment-Proof");
        if (proof == null || proof.isBlank()) {
            proof = request.getHeader("X-Payment-Proof");
        }
        if (proof == null || proof.isBlank()) {
            String auth = request.getHeader("Authorization");
            if (auth != null && auth.toLowerCase().startsWith("x402 ")) {
                proof = auth.substring(5).trim();
            }
        }
        return proof;
    }

    private void issuePaymentRequiredChallenge(HttpServletRequest request, HttpServletResponse response, String uri) throws Exception {
        double price = pricingEngine.calculatePriceForWorkflow("wf-request", 3, 30);
        X402Challenge challenge = paymentProvider.generateChallenge("wf-request", price);
        securityService.storeChallenge(challenge);

        auditService.audit("CHALLENGE_CREATED", challenge.getWorkflowId(), null, "Generated 402 challenge for price $" + price);

        response.setStatus(HttpStatus.PAYMENT_REQUIRED.value());
        response.setHeader("X-402-Payment-Required", "true");
        response.setHeader("X-402-Challenge-Id", challenge.getChallengeId());
        response.setHeader("X-402-Price", String.format("%.2f", price));
        response.setHeader("X-402-Asset", challenge.getAsset());
        response.setHeader("WWW-Authenticate", "x402 challenge_id=\"" + challenge.getChallengeId() + "\", asset=\"" + challenge.getAsset() + "\", price=\"" + price + "\"");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "HTTP 402 Payment Required");
        body.put("status", 402);
        body.put("message", "Pay-per-use x402 payment required prior to workflow execution");
        body.put("challenge", challenge);

        writeJsonResponse(response, body);
    }

    private X402PaymentProof parsePaymentProof(String headerVal) {
        try {
            if (headerVal.startsWith("{")) {
                return objectMapper.readValue(headerVal, X402PaymentProof.class);
            } else {
                // Key-value or base64 token string
                String decoded = headerVal.contains("=") && !headerVal.contains(":") ?
                        new String(Base64.getDecoder().decode(headerVal)) : headerVal;
                if (decoded.startsWith("{")) {
                    return objectMapper.readValue(decoded, X402PaymentProof.class);
                }
                // Fallback format: txId:challengeId:amount:sender
                String[] parts = decoded.split(":");
                X402PaymentProof proof = new X402PaymentProof();
                proof.setTransactionId(parts[0]);
                if (parts.length > 1) proof.setChallengeId(parts[1]);
                if (parts.length > 2) proof.setAmount(Double.parseDouble(parts[2]));
                if (parts.length > 3) proof.setSenderAddress(parts[3]);
                return proof;
            }
        } catch (Exception e) {
            log.warn("Failed to parse x402 payment proof header: {}", e.getMessage());
            return null;
        }
    }

    private void writeJsonResponse(HttpServletResponse response, Map<String, Object> data) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(data));
        writer.flush();
    }
}
