package com.example.jobagent.model;

import java.util.Map;

public record ApplicationDraft(
        String jobId,
        String summary,
        String coverLetter,
        Map<String, String> screeningAnswers) {
}