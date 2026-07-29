package com.agentmesh.router.service;

import com.agentmesh.router.dto.QuoteResponse;

import java.util.List;

public interface QuoteService {
    List<QuoteResponse> getQuotesForWorkflow(String workflowId);
    QuoteResponse getQuoteById(String id);
}
