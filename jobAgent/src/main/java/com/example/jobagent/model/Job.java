package com.example.jobagent.model;

public record Job(
        String id,
        String source,
        String title,
        String company,
        String location,
        String url,
        String description,
        String applyUrl) {
}