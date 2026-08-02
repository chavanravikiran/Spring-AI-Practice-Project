package com.example.jobagent.model;

public record JobAssessment(
        String jobId,
        int score,
        String reason) {
}
