package com.agentmesh.router.dto;

public class WorkflowRequestDto {
    private String prompt;

    public WorkflowRequestDto() {}
    public WorkflowRequestDto(String prompt) { this.prompt = prompt; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
}
