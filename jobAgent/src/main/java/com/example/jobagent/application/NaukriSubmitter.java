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
public class NaukriSubmitter implements ApplicationSubmitter {

    private static final Logger log = LoggerFactory.getLogger(NaukriSubmitter.class);

    private final BrowserSession browser;

    public NaukriSubmitter(BrowserSession browser) {
        this.browser = browser;
    }

    @Override
    public String source() {
        return "naukri";
    }

    @Override
    public ApplyResult submit(Job job, ApplicationDraft draft, CandidateProfile profile) {
        WebDriver driver = browser.driver();
        try {
            browser.open(job.url());
            browser.sleep(4000);
            WebElement apply = FormFiller.findButton(driver, "Apply");
            if (apply == null) {
                return new ApplyResult(job.id(), false, "Apply button not found; login or external redirect required");
            }
            apply.click();
            browser.sleep(3000);
            if (!driver.getCurrentUrl().startsWith("https://www.naukri.com")) {
                return new ApplyResult(job.id(), false, "Redirected to external careers site: " + driver.getCurrentUrl());
            }
            FormFiller.fillVisibleFields(driver, draft, profile);
            WebElement submit = FormFiller.findButton(driver, "Submit");
            if (submit == null) {
                submit = FormFiller.findButton(driver, "Send");
            }
            if (submit != null) {
                submit.click();
                browser.sleep(2000);
                return new ApplyResult(job.id(), true, "Submitted via Naukri");
            }
            return new ApplyResult(job.id(), false, "Fields filled but no confirm button found");
        } catch (Exception e) {
            log.warn("naukri submit failed for {}: {}", job.id(), e.getMessage());
            return new ApplyResult(job.id(), false, e.getMessage());
        }
    }
}