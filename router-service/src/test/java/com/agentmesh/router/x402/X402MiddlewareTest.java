package com.agentmesh.router.x402;

import com.agentmesh.router.x402.dto.X402Challenge;
import com.agentmesh.router.x402.dto.X402PaymentProof;
import com.agentmesh.router.x402.middleware.X402PaymentInterceptor;
import com.agentmesh.router.x402.pricing.X402DynamicPricingEngine;
import com.agentmesh.router.x402.provider.AlgorandX402Provider;
import com.agentmesh.router.x402.service.X402AuditService;
import com.agentmesh.router.x402.service.X402PaymentMetrics;
import com.agentmesh.router.x402.service.X402SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class X402MiddlewareTest {

    private AlgorandX402Provider paymentProvider;
    private X402DynamicPricingEngine pricingEngine;
    private X402SecurityService securityService;
    private X402AuditService auditService;
    private X402PaymentMetrics metrics;
    private X402PaymentInterceptor interceptor;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        paymentProvider = new AlgorandX402Provider(
                "D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ",
                "https://facilitator.goplausible.xyz",
                "31566704",
                restTemplate
        );
        pricingEngine = new X402DynamicPricingEngine(1.0, 0.5, 0.05);
        securityService = new X402SecurityService();
        auditService = new X402AuditService();
        metrics = new X402PaymentMetrics();

        interceptor = new X402PaymentInterceptor(
                paymentProvider, pricingEngine, securityService, auditService, metrics
        );
    }

    @Test
    void testPreHandleMissingProofReturns402Challenge() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/api/execution/start");
        when(request.getMethod()).thenReturn("POST");

        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        boolean continueChain = interceptor.preHandle(request, response, new Object());

        assertFalse(continueChain);
        verify(response).setStatus(402);
        verify(response).setHeader(eq("X-402-Payment-Required"), eq("true"));
        assertTrue(responseWriter.toString().contains("HTTP 402 Payment Required"));
    }

    @Test
    void testPreHandleWithValidPaymentProofPasses() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/api/execution/start");
        when(request.getMethod()).thenReturn("POST");

        X402PaymentProof proof = new X402PaymentProof("ch-1", "TX-MW-100", "SENDERADDR", 5.0, "sig-mw");
        String proofJson = new ObjectMapper().writeValueAsString(proof);

        when(request.getHeader("X-402-Payment-Proof")).thenReturn(proofJson);

        boolean continueChain = interceptor.preHandle(request, response, new Object());

        assertTrue(continueChain);
        verify(response, never()).setStatus(402);
    }
}
