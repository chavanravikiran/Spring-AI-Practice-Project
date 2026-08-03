package com.example.jobagent.application;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.jobagent.browser.BrowserSession;
import com.example.jobagent.browser.FormFiller;
import com.example.jobagent.model.ApplicationDraft;
import com.example.jobagent.model.ApplyResult;
import com.example.jobagent.model.CandidateProfile;
import com.example.jobagent.model.Job;

@Component
public class LinkedInSubmitter implements ApplicationSubmitter {

    private static final Logger log = LoggerFactory.getLogger(LinkedInSubmitter.class);

    private final BrowserSession browser;

    public LinkedInSubmitter(BrowserSession browser) {
        this.browser = browser;
    }

    @Override
    public String source() {
        return "linkedin";
    }

//    @Override
//    public ApplyResult submit(Job job, ApplicationDraft draft, CandidateProfile profile) {
//        WebDriver driver = browser.driver();
//        try {
//            browser.open(job.url());
//            browser.sleep(3000);
//            WebElement easyApply = FormFiller.findButton(driver, "Easy Apply");
//            if (easyApply == null) {
//                return new ApplyResult(job.id(), false, "Easy Apply button not found");
//            }
//            easyApply.click();
//            browser.sleep(2000);
//            int guard = 0;
//            while (guard++ < 12) {
//                FormFiller.fillVisibleFields(driver, draft, profile);
//                WebElement submit = FormFiller.findButton(driver, "Submit application");
//                if (submit != null) {
//                    submit.click();
//                    browser.sleep(2000);
//                    return new ApplyResult(job.id(), true, "Submitted via Easy Apply");
//                }
//                WebElement next = FormFiller.findButton(driver, "Next");
//                if (next == null) {
//                    next = FormFiller.findButton(driver, "Review");
//                }
//                if (next == null) {
//                    return new ApplyResult(job.id(), false, "Easy Apply flow could not continue");
//                }
//                next.click();
//                browser.sleep(1500);
//            }
//            return new ApplyResult(job.id(), false, "Easy Apply flow exceeded step limit");
//        } catch (Exception e) {
//            log.warn("linkedin submit failed for {}: {}", job.id(), e.getMessage());
//            return new ApplyResult(job.id(), false, e.getMessage());
//        }
//    }
    @Override
    public ApplyResult submit(Job job, ApplicationDraft draft, CandidateProfile profile) {

        WebDriver driver = browser.driver();

        try {

            browser.open(job.url());

            browser.sleep(6000);

            System.out.println("-------------------------------------");
            System.out.println("Job URL     : " + driver.getCurrentUrl());
            System.out.println("Page Title  : " + driver.getTitle());

            // Print all visible buttons for debugging
            for (WebElement button : driver.findElements(org.openqa.selenium.By.tagName("button"))) {
                try {
                    String text = button.getText().trim();
                    if (!text.isBlank()) {
                        System.out.println("Button Found : " + text);
                    }
                } catch (Exception ignored) {
                }
            }

            WebElement applyButton = FormFiller.findButton(driver, "Easy Apply");

            if (applyButton == null) {
                applyButton = FormFiller.findButton(driver, "Apply");
            }

            if (applyButton == null) {
                applyButton = FormFiller.findButton(driver, "Apply now");
            }

            if (applyButton == null) {
                applyButton = FormFiller.findButton(driver, "Quick Apply");
            }

            if (applyButton == null) {
                return new ApplyResult(
                        job.id(),
                        false,
                        "No Easy Apply / Apply button found");
            }

            applyButton.click();

            browser.sleep(3000);

            int guard = 0;

            while (guard++ < 15) {

                FormFiller.fillVisibleFields(driver, draft, profile);

                WebElement submit = FormFiller.findButton(driver, "Submit application");

                if (submit == null)
                    submit = FormFiller.findButton(driver, "Submit");

                if (submit == null)
                    submit = FormFiller.findButton(driver, "Send application");

                if (submit != null) {

                    submit.click();

                    browser.sleep(3000);

                    return new ApplyResult(
                            job.id(),
                            true,
                            "Application submitted successfully");
                }

                WebElement next = FormFiller.findButton(driver, "Next");

                if (next == null)
                    next = FormFiller.findButton(driver, "Continue");

                if (next == null)
                    next = FormFiller.findButton(driver, "Review");

                if (next == null) {
                    return new ApplyResult(
                            job.id(),
                            false,
                            "No Next/Review/Submit button found");
                }

                next.click();

                browser.sleep(2500);
            }

            return new ApplyResult(
                    job.id(),
                    false,
                    "Exceeded maximum Easy Apply steps");

        } catch (Exception e) {

            log.error("LinkedIn Apply Error", e);

            return new ApplyResult(
                    job.id(),
                    false,
                    e.getMessage());
        }
    }
}