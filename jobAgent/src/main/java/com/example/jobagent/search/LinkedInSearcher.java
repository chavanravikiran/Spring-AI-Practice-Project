package com.example.jobagent.search;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.jobagent.browser.BrowserSession;
import com.example.jobagent.model.Job;

@Component
public class LinkedInSearcher implements JobSearcher {

    private static final String CARD_SELECTOR = ".job-card-container";
    private static final String TITLE_SELECTOR = ".job-card-list__title";
    private static final String COMPANY_SELECTOR = ".artdeco-entity-lockup__subtitle";
    private static final String LOCATION_SELECTOR = ".job-card-container__metadata-wrapper .job-card-container__primary-description";

    private static final Logger log = LoggerFactory.getLogger(LinkedInSearcher.class);

    private final BrowserSession browser;

    public LinkedInSearcher(BrowserSession browser) {
        this.browser = browser;
    }

    @Override
    public String source() {
        return "linkedin";
    }

    @Override
    public List<Job> search(String keywords, String location, int maxJobs) {
        String q = URLEncoder.encode(keywords, StandardCharsets.UTF_8);
        String loc = URLEncoder.encode(location, StandardCharsets.UTF_8);
        String url = "https://www.linkedin.com/jobs/search?keywords=" + q + "&location=" + loc;
        browser.open(url);
        try {
            browser.waitFor(By.cssSelector(CARD_SELECTOR), 20);
        } catch (TimeoutException e) {
            log.warn("linkedin: no job cards found for {}", url);
            return List.of();
        }
        browser.randomDelay();
        List<WebElement> cards = browser.driver().findElements(By.cssSelector(CARD_SELECTOR));
        List<Job> jobs = new ArrayList<>();
        for (WebElement card : cards) {
            if (jobs.size() >= maxJobs) {
                break;
            }
            try {
                WebElement titleEl = card.findElement(By.cssSelector(TITLE_SELECTOR));
                String title = titleEl.getText().trim();
                String href = titleEl.getAttribute("href");
                if (href != null && href.startsWith("/")) {
                    href = "https://www.linkedin.com" + href;
                }
                String company = card.findElement(By.cssSelector(COMPANY_SELECTOR)).getText().trim();
                String where = card.findElement(By.cssSelector(LOCATION_SELECTOR)).getText().trim();
                String id = href != null ? href : "linkedin-" + title;
                jobs.add(new Job(id, source(), title, company, where, href, "", href));
            } catch (Exception ex) {
                log.debug("linkedin: skip card: {}", ex.getMessage());
            }
            browser.randomDelay();
        }
        return jobs;
    }
}