package com.example.jobagent.application;

import com.example.jobagent.model.ApplicationDraft;
import com.example.jobagent.model.ApplyResult;
import com.example.jobagent.model.CandidateProfile;
import com.example.jobagent.model.Job;

public interface ApplicationSubmitter {

    String source();

    ApplyResult submit(Job job, ApplicationDraft draft, CandidateProfile profile);
}