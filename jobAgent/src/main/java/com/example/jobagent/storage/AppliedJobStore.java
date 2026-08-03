package com.example.jobagent.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Component;
import com.example.jobagent.config.AgentProperties;
import com.example.jobagent.model.ApplyResult;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Component
public class AppliedJobStore {

    private final ObjectMapper objectMapper;
    private final Path file;
    private final ConcurrentMap<String, ApplyResult> applied = new ConcurrentHashMap<>();

    public AppliedJobStore(AgentProperties props, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.file = Path.of(props.storage.path);
        load();
    }

    public boolean has(String jobId) {
        return applied.containsKey(jobId);
    }

    public void record(String jobId, boolean success, String detail) {
        applied.put(jobId, new ApplyResult(jobId, success, detail));
        save();
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
//        try {
//            JsonNode root = objectMapper.readTree(file.toFile());
//            root.fields().forEachRemaining(e -> {
//                JsonNode value = e.getValue();
//                applied.put(e.getKey(),
//                        new ApplyResult(e.getKey(),
//                                value.path("success").asBoolean(false),
//                                value.path("detail").asText("")));
//            });
        	JsonNode root = objectMapper.readTree(file.toFile());
        	if (root.isObject()) {
        		((ObjectNode) root).properties().forEach(e -> {
        			JsonNode value = e.getValue();
        			applied.put(e.getKey(),
        					new ApplyResult(e.getKey(),
        							value.path("success").asBoolean(false),
        							value.path("detail").asText("")));
        		});
        	}
//        } 
//        catch (IOException e) {
//            throw new IllegalStateException("cannot load applied jobs store", e);
//        }
    }

    private void save() {
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), applied);
        } catch (IOException e) {
            throw new IllegalStateException("cannot save applied jobs store", e);
        }
    }
}