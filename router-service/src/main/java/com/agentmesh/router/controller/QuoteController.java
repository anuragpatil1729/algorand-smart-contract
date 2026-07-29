package com.agentmesh.router.controller;

import com.agentmesh.router.dto.ApiResponse;
import com.agentmesh.router.dto.QuoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/quotes")
@Tag(name = "Quote Controller", description = "Endpoints for retrieving AI agent cost and ETA quotations")
public class QuoteController {

    @GetMapping("/workflow/{workflowId}")
    @Operation(summary = "Get all agent quotes for tasks in a workflow")
    public ResponseEntity<ApiResponse<List<QuoteResponse>>> getQuotesForWorkflow(@PathVariable String workflowId) {
        return ResponseEntity.ok(ApiResponse.success("Quotes retrieved successfully", new ArrayList<>()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get quote details by ID")
    public ResponseEntity<ApiResponse<QuoteResponse>> getQuoteById(@PathVariable String id) {
        QuoteResponse placeholder = new QuoteResponse(
                id,
                "wf-sample",
                "agent-research-01",
                "Research & Market Intelligence Agent",
                45.0,
                96.0,
                10,
                98.5
        );
        return ResponseEntity.ok(ApiResponse.success(placeholder));
    }
}
