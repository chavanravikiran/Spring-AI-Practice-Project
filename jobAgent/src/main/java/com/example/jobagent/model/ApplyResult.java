package com.example.jobagent.model;

public record ApplyResult(
        String jobId,
        boolean success,
        String detail) {
}
