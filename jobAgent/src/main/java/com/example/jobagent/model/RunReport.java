package com.example.jobagent.model;

import java.util.List;

public record RunReport(
        String timestamp,
        int jobsFound,
        int jobsAssessed,
        List<JobAssessment> matched,
        List<ApplyResult> applyResults,
        String message) {
}
