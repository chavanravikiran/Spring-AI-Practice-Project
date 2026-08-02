package com.example.jobagent.model;

import java.util.List;

public record CandidateProfile(
        String name,
        String email,
        String phone,
        String headline,
        String location,
        String linkedin,
        List<String> skills,
        List<String> experience,
        List<String> education,
        String targetRole,
        String workAuthorization) {
}