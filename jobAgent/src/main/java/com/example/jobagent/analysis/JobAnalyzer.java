package com.example.jobagent.analysis;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import com.example.jobagent.config.AgentProperties;
import com.example.jobagent.model.ApplicationDraft;
import com.example.jobagent.model.CandidateProfile;
import com.example.jobagent.model.Job;
import com.example.jobagent.model.JobAssessment;
import com.example.jobagent.util.JsonUtil;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

@Component
public class JobAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(JobAnalyzer.class);
    private static final int DESCRIPTION_LIMIT = 3000;

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public JobAnalyzer(ChatClient.Builder builder, ObjectMapper objectMapper, AgentProperties props) {
        this.chatClient = builder.build();
        this.objectMapper = objectMapper;
    }

    public JobAssessment assess(Job job, CandidateProfile profile) {
        String user = """
                You are a senior recruiter scoring a candidate's fit for a specific job.

                CANDIDATE PROFILE:
                %s

                JOB TITLE: %s
                COMPANY: %s
                LOCATION: %s
                DESCRIPTION:
                %s

                Respond with ONLY a JSON object, no markdown, no extra text:
                {"score": <integer 0-10>, "reason": "<one sentence>"}
                """.formatted(safe(toJson(profile)), safe(job.title()), safe(job.company()),
                safe(job.location()), safe(truncate(job.description())));
        String content = chatClient.prompt().user(user).call().content();
        return parseAssessment(job.id(), content);
    }

    public ApplicationDraft draft(Job job, CandidateProfile profile) {
        String user = """
                You are a professional job application writer. Tailor an application for the candidate.

                CANDIDATE PROFILE:
                %s

                JOB TITLE: %s
                COMPANY: %s
                DESCRIPTION:
                %s

                Respond with ONLY a JSON object, no markdown, no extra text:
                {
                  "summary": "2-3 sentence professional summary targeted to this job",
                  "coverLetter": "4-6 sentence cover letter using the candidate's real experience",
                  "screeningAnswers": {
                    "Why are you a good fit for this role?": "one paragraph",
                    "Tell us about a relevant achievement.": "one paragraph"
                  }
                }
                """.formatted(safe(toJson(profile)), safe(job.title()), safe(job.company()),
                safe(truncate(job.description())));
        String content = chatClient.prompt().user(user).call().content();
        return parseDraft(job.id(), content);
    }

    private JobAssessment parseAssessment(String jobId, String raw) {
        try {
            JsonNode node = objectMapper.readTree(JsonUtil.extractJson(raw));
            int score = node.path("score").asInt(0);
            String reason = node.path("reason").asText("");
            return new JobAssessment(jobId, score, reason);
        } catch (Exception e) {
            log.warn("could not parse assessment for {}: {}", jobId, raw);
            return new JobAssessment(jobId, 0, "parse failure");
        }
    }

    private ApplicationDraft parseDraft(String jobId, String raw) {
        try {
            JsonNode node = objectMapper.readTree(JsonUtil.extractJson(raw));
            Map<String, String> answers = new LinkedHashMap<>();
            JsonNode qa = node.path("screeningAnswers");
            if (qa.isObject()) {
                ((ObjectNode) qa).properties().forEach(e -> answers.put(e.getKey(), e.getValue().asText("")));
            }
//            if (qa.isObject()) {
//                qa.fields().forEachRemaining(e -> answers.put(e.getKey(), e.getValue().asText("")));
//            }
            return new ApplicationDraft(jobId,
                    node.path("summary").asText(""),
                    node.path("coverLetter").asText(""),
                    answers);
        } catch (Exception e) {
            log.warn("could not parse draft for {}: {}", jobId, raw);
            return new ApplicationDraft(jobId, "", "", Map.of());
        }
    }

    private String toJson(CandidateProfile profile) {
        try {
            return objectMapper.writeValueAsString(profile);
        } catch (Exception e) {
            return profile.toString();
        }
    }

    private String truncate(String text) {
        if (text == null || text.length() <= DESCRIPTION_LIMIT) {
            return text;
        }
        return text.substring(0, DESCRIPTION_LIMIT);
    }

    private String safe(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("[\\p{Cntrl}]", " ");
    }
}