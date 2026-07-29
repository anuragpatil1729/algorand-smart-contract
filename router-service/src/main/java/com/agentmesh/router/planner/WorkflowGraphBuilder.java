package com.agentmesh.router.planner;

import com.agentmesh.router.planner.dto.PlannedTaskDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WorkflowGraphBuilder {

    public Map<String, Object> buildGraphRepresentation(List<PlannedTaskDto> tasks) {
        Map<String, Object> graph = new LinkedHashMap<>();
        if (tasks == null || tasks.isEmpty()) return graph;

        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, String>> edges = new ArrayList<>();

        for (PlannedTaskDto t : tasks) {
            Map<String, Object> node = new LinkedHashMap<>();
            node.put("id", t.getTaskId());
            node.put("name", t.getTaskName());
            node.put("capability", t.getRequiredCapability());
            node.put("stage", t.getExecutionStage());
            node.put("complexity", t.getComplexity());
            nodes.add(node);

            if (t.getDependencies() != null) {
                for (String dep : t.getDependencies()) {
                    Map<String, String> edge = new LinkedHashMap<>();
                    edge.put("source", dep);
                    edge.put("target", t.getTaskId());
                    edges.add(edge);
                }
            }
        }

        graph.put("nodeCount", nodes.size());
        graph.put("edgeCount", edges.size());
        graph.put("nodes", nodes);
        graph.put("edges", edges);

        return graph;
    }
}
