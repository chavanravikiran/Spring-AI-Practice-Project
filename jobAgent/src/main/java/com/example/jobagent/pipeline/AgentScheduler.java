package com.example.jobagent.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.jobagent.config.AgentProperties;

@Component
public class AgentScheduler {

	private static final Logger log = LoggerFactory.getLogger(AgentScheduler.class);

	private final AgentPipeline pipeline;
	private final AgentProperties props;

	public AgentScheduler(AgentPipeline pipeline, AgentProperties props) {
		this.pipeline = pipeline;
		this.props = props;
	}

	@Scheduled(cron = "${jobagent.run.cron:0 0 9 * * MON-FRI}")
	public void scheduledRun() {
		if (!props.run.enabled) {
			return;
		}
		log.info("scheduled agent run starting");
		pipeline.run();
	}
}