package com.example.jobagent.search;

import java.util.List;

import com.example.jobagent.model.Job;

public interface JobSearcher {

    String source();

    List<Job> search(String keywords, String location, int maxJobs);
}