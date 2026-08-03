package com.example.jobagent.search;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.jobagent.browser.BrowserSession;
import com.example.jobagent.model.Job;

@Component
public class NaukriSearcher implements JobSearcher {

	@Value("${jobagent.search.max-jobs-per-source}")
	private int maxJobsPerSource;
	
    private static final String CARD_SELECTOR = ".srp-jobtuple-wrapper";
    private static final String TITLE_SELECTOR = ".title.ellipsis";
//    private static final String COMPANY_SELECTOR = ".subTitle.ellipsis";
//    private static final String LOCATION_SELECTOR = ".fleft.grey-text.br2.placeHolderLi.location";
    private static final String COMPANY_SELECTOR = "a.comp-name, .subTitle.ellipsis";
    private static final String LOCATION_SELECTOR = ".loc-wrap, .fleft.grey-text.br2.placeHolderLi.location";

    private static final Logger log = LoggerFactory.getLogger(NaukriSearcher.class);

    private final BrowserSession browser;

    public NaukriSearcher(BrowserSession browser) {
        this.browser = browser;
    }

    @Override
    public String source() {
        return "naukri";
    }

    @Override
    public List<Job> search(String keywords, String location, int maxJobs) {
        String path = keywords.trim().toLowerCase().replaceAll("\\s+", "-");
        String url = "https://www.naukri.com/" + path + "-jobs";
        if (location != null && !location.isBlank()) {
            url += "-in-" + location.trim().toLowerCase().replaceAll("\\s+", "-");
        }
        browser.open(url);
        try {
            browser.waitFor(By.cssSelector(CARD_SELECTOR), this.maxJobsPerSource);
        } catch (TimeoutException e) {
            log.warn("naukri: no job cards found for {}", url);
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
                WebElement anchor = titleEl.findElement(By.tagName("a"));
                String title = anchor.getText().trim();
                String href = anchor.getAttribute("href");
                if (href != null && href.startsWith("/")) {
                    href = "https://www.naukri.com" + href;
                }
                String company = card.findElement(By.cssSelector(COMPANY_SELECTOR)).getText().trim();
                String where = card.findElement(By.cssSelector(LOCATION_SELECTOR)).getText().trim();
                String id = href != null ? href : "naukri-" + title;
                jobs.add(new Job(id, source(), title, company, where, href, "", href));
            } catch (Exception ex) {
                log.debug("naukri: skip card: {}", ex.getMessage());
            }
            browser.randomDelay();
        }
        return jobs;
    }
}
