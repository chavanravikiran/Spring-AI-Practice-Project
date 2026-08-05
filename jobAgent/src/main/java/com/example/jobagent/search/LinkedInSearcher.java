//package com.example.jobagent.search;
//
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.TimeoutException;
//import org.openqa.selenium.WebElement;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import com.example.jobagent.browser.BrowserSession;
//import com.example.jobagent.model.Job;
//
//@Component
//public class LinkedInSearcher implements JobSearcher {
//
//    private static final String CARD_SELECTOR = ".job-card-container";
//    private static final String TITLE_SELECTOR = ".job-card-list__title";
////    private static final String COMPANY_SELECTOR = ".artdeco-entity-lockup__subtitle";
////    private static final String LOCATION_SELECTOR = ".job-card-container__metadata-wrapper .job-card-container__primary-description";
//
//    private static final String COMPANY_SELECTOR = ".job-card-container__company-name, .artdeco-entity-lockup__subtitle";
//    private static final String LOCATION_SELECTOR = ".job-card-container__metadata-item, .job-card-container__metadata-wrapper .job-card-container__primary-description";
//    
//    private static final Logger log = LoggerFactory.getLogger(LinkedInSearcher.class);
//
//    private final BrowserSession browser;
//
//    public LinkedInSearcher(BrowserSession browser) {
//        this.browser = browser;
//    }
//
//    @Override
//    public String source() {
//        return "linkedin";
//    }
//
//    @Override
//    public List<Job> search(String keywords, String location, int maxJobs) {
//        String q = URLEncoder.encode(keywords, StandardCharsets.UTF_8);
//        String loc = URLEncoder.encode(location, StandardCharsets.UTF_8);
//        String url = "https://www.linkedin.com/jobs/search?keywords=" + q + "&location=" + loc;
//        System.out.println("------------>"+url);
//        browser.open(url);
//        try {
////            browser.waitFor(By.cssSelector(CARD_SELECTOR),20);
//        	browser.sleep(8000);
//
//        	System.out.println("--------->LinkedIn---->"+browser.driver().getCurrentUrl());
//        	System.out.println("--------->LinkedIn Title---->"+browser.driver().getTitle());
//
//        	List<WebElement> cards =
//        	browser.driver().findElements(By.cssSelector(CARD_SELECTOR));
//
//        	System.out.println("Cards = " + cards.size());
//        } catch (TimeoutException e) {
//            log.warn("linkedin: no job cards found for {}", url);
//            return List.of();
//        }
//        browser.randomDelay();
//        List<WebElement> cards = browser.driver().findElements(By.cssSelector(CARD_SELECTOR));
//        List<Job> jobs = new ArrayList<>();
//        for (WebElement card : cards) {
//            if (jobs.size() >= maxJobs) {
//                break;
//            }
//            try {
//                WebElement titleEl = card.findElement(By.cssSelector(TITLE_SELECTOR));
//                String title = titleEl.getText().trim();
//                String href = titleEl.getAttribute("href");
//                if (href != null && href.startsWith("/")) {
//                    href = "https://www.linkedin.com" + href;
//                }
//                String company = card.findElement(By.cssSelector(COMPANY_SELECTOR)).getText().trim();
//                String where = card.findElement(By.cssSelector(LOCATION_SELECTOR)).getText().trim();
//                String id = href != null ? href : "linkedin-" + title;
//                jobs.add(new Job(id, source(), title, company, where, href, "", href));
//            } catch (Exception ex) {
//                log.debug("linkedin: skip card: {}", ex.getMessage());
//            }
//            browser.randomDelay();
//        }
//        return jobs;
//    }
//}

package com.example.jobagent.search;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.example.jobagent.browser.BrowserSession;
import com.example.jobagent.model.Job;

@Component
public class LinkedInSearcher implements JobSearcher {

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

        List<Job> jobs = new ArrayList<>();

        try {

            String url = "https://www.linkedin.com/jobs/search/?keywords="
                    + URLEncoder.encode(keywords, StandardCharsets.UTF_8)
                    + "&location="
                    + URLEncoder.encode(location, StandardCharsets.UTF_8);

            System.out.println("Opening : " + url);

//            browser.open(url);

            browser.sleep(8000);

            String currentUrl = browser.driver().getCurrentUrl();
            String title = browser.driver().getTitle();

            System.out.println("Current URL : " + currentUrl);
            System.out.println("Page Title  : " + title);

            if (currentUrl.contains("/login")
                    || currentUrl.contains("/checkpoint")
                    || title.toLowerCase().contains("sign in")) {

                System.out.println("LinkedIn redirected to Login page.");
                return jobs;
            }

            List<WebElement> cards = new ArrayList<>();

            String[] selectors = {
                    ".job-card-container",
                    ".jobs-search-results__list-item",
                    "li.scaffold-layout__list-item",
                    "li.jobs-search-results__list-item",
                    ".jobs-search__results-list li"
            };

            for (String selector : selectors) {

                cards = browser.driver().findElements(By.cssSelector(selector));

                System.out.println(selector + " -> " + cards.size());

                if (!cards.isEmpty()) {
                    break;
                }
            }

            if (cards.isEmpty()) {
                System.out.println("No job cards found.");
                return jobs;
            }

            for (WebElement card : cards) {

                if (jobs.size() >= maxJobs)
                    break;

                try {

                    String jobTitle = "";
                    String company = "";
                    String jobLocation = "";
                    String href = "";

                    try {
                        WebElement a = card.findElement(By.cssSelector("a[href*='/jobs/view/']"));
                        href = a.getAttribute("href");
                        jobTitle = a.getText().trim();
                    } catch (Exception ignored) {
                    }

                    if (jobTitle.isBlank()) {
                        try {
                            jobTitle = card.findElement(By.tagName("h3")).getText().trim();
                        } catch (Exception ignored) {
                        }
                    }

                    if (company.isBlank()) {
                        try {
                            company = card.findElement(By.cssSelector(".artdeco-entity-lockup__subtitle"))
                                    .getText().trim();
                        } catch (Exception ignored) {
                        }
                    }

                    if (company.isBlank()) {
                        try {
                            company = card.findElement(By.cssSelector(".job-card-container__company-name"))
                                    .getText().trim();
                        } catch (Exception ignored) {
                        }
                    }

                    if (jobLocation.isBlank()) {
                        try {
                            jobLocation = card.findElement(By.cssSelector(".job-card-container__metadata-item"))
                                    .getText().trim();
                        } catch (Exception ignored) {
                        }
                    }

                    if (jobLocation.isBlank()) {
                        try {
                            jobLocation = card.findElement(By.cssSelector(".job-card-container__primary-description"))
                                    .getText().trim();
                        } catch (Exception ignored) {
                        }
                    }

                    if (href == null || href.isBlank()) {
                        continue;
                    }

                    String id = href;

                    jobs.add(new Job(
                            id,
                            source(),
                            jobTitle,
                            company,
                            jobLocation,
                            href,
                            "",
                            href));

                    System.out.println("----------------------------------------");
                    System.out.println("Title    : " + jobTitle);
                    System.out.println("Company  : " + company);
                    System.out.println("Location : " + jobLocation);
                    System.out.println("URL      : " + href);

                } catch (Exception e) {

                    System.out.println("Skipping card : " + e.getMessage());

                }

            }

            System.out.println("LinkedIn Jobs Found : " + jobs.size());

        } catch (Exception e) {

            e.printStackTrace();

        }

        return jobs;
    }

}