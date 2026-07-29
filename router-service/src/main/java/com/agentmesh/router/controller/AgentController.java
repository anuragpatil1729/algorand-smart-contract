package com.agentmesh.router.controller;

import com.agentmesh.router.dto.AgentDto;
import com.agentmesh.router.dto.AgentRegistrationDto;
import com.agentmesh.router.service.AgentMeshService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentMeshService agentMeshService;

    public AgentController(AgentMeshService agentMeshService) {
        this.agentMeshService = agentMeshService;
    }

    @GetMapping
    public ResponseEntity<List<AgentDto>> listAgents(@RequestParam(required = false) String capability) {
        List<AgentDto> agents = agentMeshService.listAgents(capability);
        return ResponseEntity.ok(agents);
    }

    @PostMapping("/register")
    public ResponseEntity<AgentDto> registerAgent(@RequestBody AgentRegistrationDto registrationDto) {
        AgentDto agent = agentMeshService.registerAgent(registrationDto);
        return ResponseEntity.ok(agent);
    }
}
