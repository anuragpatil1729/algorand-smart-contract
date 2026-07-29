package com.agentmesh.router.dto;

import jakarta.validation.constraints.NotBlank;

public class WorkflowRequest {

    @NotBlank(message = "Prompt cannot be empty")
    private String prompt;

    public WorkflowRequest() {}
    public WorkflowRequest(String prompt) { this.prompt = prompt; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
}
