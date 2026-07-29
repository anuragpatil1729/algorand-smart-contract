package com.agentmesh.router.dto;

public class WorkflowRequest {
    private String prompt;

    public WorkflowRequest() {}
    public WorkflowRequest(String prompt) { this.prompt = prompt; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
}
