package com.example.jobagent.pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import com.example.jobagent.analysis.JobAnalyzer;
import com.example.jobagent.application.ApplicationSubmitter;
import com.example.jobagent.config.AgentProperties;
import com.example.jobagent.model.ApplicationDraft;
import com.example.jobagent.model.ApplyResult;
import com.example.jobagent.model.CandidateProfile;
import com.example.jobagent.model.Job;
import com.example.jobagent.model.JobAssessment;
import com.example.jobagent.model.RunReport;
import com.example.jobagent.search.JobSearcher;
import com.example.jobagent.storage.AppliedJobStore;
import tools.jackson.databind.ObjectMapper;

@Component
public class AgentPipeline {

	private static final Logger log = LoggerFactory.getLogger(AgentPipeline.class);

	private final List<JobSearcher> searchers;
	private final List<ApplicationSubmitter> submitters;
	private final JobAnalyzer analyzer;
	private final AppliedJobStore store;
	private final AgentProperties props;
	private final ObjectMapper objectMapper;
	private final ResourceLoader resourceLoader;

	public AgentPipeline(List<JobSearcher> searchers, List<ApplicationSubmitter> submitters, JobAnalyzer analyzer,
			AppliedJobStore store, AgentProperties props, ObjectMapper objectMapper, ResourceLoader resourceLoader) {
		this.searchers = searchers;
		this.submitters = submitters;
		this.analyzer = analyzer;
		this.store = store;
		this.props = props;
		this.objectMapper = objectMapper;
		this.resourceLoader = resourceLoader;
	}

	public RunReport run() {
		CandidateProfile profile = loadProfile();
		List<Job> found = new ArrayList<>();
		for (JobSearcher searcher : searchers) {
			for (String keyword : props.search.keywords) {
				try {
					found.addAll(searcher.search(keyword, props.search.location, props.search.maxJobsPerSource));
				} catch (Exception e) {
					log.warn("search failed on {} for {}: {}", searcher.source(), keyword, e.getMessage());
				}
			}
		}
		List<Job> jobs = dedupe(found);

		List<JobAssessment> assessed = new ArrayList<>();
		List<JobAssessment> matched = new ArrayList<>();
		for (Job job : jobs) {
			if (store.has(job.id())) {
				continue;
			}
			JobAssessment assessment = analyzer.assess(job, profile);
			assessed.add(assessment);
			if (assessment.score() >= props.analyze.minScore) {
				matched.add(assessment);
			}
		}
		matched.sort(Comparator.comparingInt(JobAssessment::score).reversed());

		int limit = props.analyze.maxApplicationsPerRun > 0
				? Math.min(props.analyze.maxApplicationsPerRun, matched.size())
				: matched.size();
		List<JobAssessment> toApply = matched.subList(0, limit);

		List<ApplyResult> results = new ArrayList<>();
		for (JobAssessment assessment : toApply) {
			Job job = findJob(jobs, assessment.jobId());
			if (job == null) {
				continue;
			}
			try {
				ApplicationDraft draft = analyzer.draft(job, profile);
				if (props.apply.autoApply) {
					ApplyResult result = applyTo(job, draft, profile);
					store.record(job.id(), result.success(), result.detail());
					results.add(result);
				} else {
					results.add(new ApplyResult(job.id(), false, "draft generated; auto-apply disabled"));
				}
			} catch (Exception e) {
				log.warn("apply failed for {}: {}", job.id(), e.getMessage());
				results.add(new ApplyResult(job.id(), false, e.getMessage()));
			}
		}

		return new RunReport(Instant.now().toString(), jobs.size(), assessed.size(), matched, results, "done");
	}

	private ApplyResult applyTo(Job job, ApplicationDraft draft, CandidateProfile profile) {
		Optional<ApplicationSubmitter> submitter = submitters.stream().filter(s -> s.source().equals(job.source()))
				.findFirst();
		return submitter.map(s -> s.submit(job, draft, profile))
				.orElseGet(() -> new ApplyResult(job.id(), false, "no submitter for source " + job.source()));
	}

	private CandidateProfile loadProfile() {
		Resource resource = resourceLoader.getResource(props.profile.path);
		try (InputStream in = resource.getInputStream()) {
			return objectMapper.readValue(in, CandidateProfile.class);
		} catch (IOException e) {
			throw new IllegalStateException("cannot load candidate profile", e);
		}
	}

	private List<Job> dedupe(List<Job> jobs) {
		Map<String, Job> unique = new LinkedHashMap<>();
		for (Job job : jobs) {
			String key = job.url() != null && !job.url().isBlank() ? job.url() : job.id();
			unique.putIfAbsent(key, job);
		}
		return new ArrayList<>(unique.values());
	}

	private Job findJob(List<Job> jobs, String id) {
		for (Job job : jobs) {
			if (job.id().equals(id)) {
				return job;
			}
		}
		return null;
	}
}