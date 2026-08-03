package com.example.jobagent.web;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobagent.model.RunReport;
import com.example.jobagent.pipeline.AgentPipeline;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentPipeline pipeline;

    public AgentController(AgentPipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping("/run")
    public RunReport run() {
        return pipeline.run();
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}