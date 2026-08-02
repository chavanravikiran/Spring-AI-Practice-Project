package com.example.jobagent.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jobagent")
public class AgentProperties {

    public Search search = new Search();
    public Analyze analyze = new Analyze();
    public Apply apply = new Apply();
    public Storage storage = new Storage();
    public Browser browser = new Browser();
    public Profile profile = new Profile();
    public Run run = new Run();

    public static class Search {
        public List<String> keywords = List.of("Java Developer");
        public String location = "";
        public int maxJobsPerSource = 20;
        public int delayMinMs = 2000;
        public int delayMaxMs = 5000;
    }

    public static class Analyze {
        public int minScore = 7;
        public int maxApplicationsPerRun = 3;
    }

    public static class Apply {
        public boolean autoApply = true;
        public boolean headless = true;
    }

    public static class Storage {
        public String path = "data/applied-jobs.json";
    }

    public static class Browser {
        public String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36";
        public String userDataDir = "";
    }

    public static class Profile {
        public String path = "classpath:profile.json";
    }

    public static class Run {
        public String cron = "0 0 9 * * MON-FRI";
        public boolean enabled = false;
    }
}