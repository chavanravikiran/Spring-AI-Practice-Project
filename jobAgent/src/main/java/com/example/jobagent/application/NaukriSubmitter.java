package com.example.jobagent.application;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
    	System.out.println("========== NAUKRI SUBMIT START ==========");
    	System.out.println("job.title:-"+job.title());
    	System.out.println("job.url:-"+job.url());
    	 WebDriver driver = browser.driver();

    	    try {
    	        // Open Naukri job page
//    	    	browser.openInNewTab("https://www.naukri.com");
    	    	browser.openInNewTab(job.url());

    	    	browser.sleep(5000);
    	    	
    	    	List<WebElement> buttons = driver.findElements(By.tagName("button"));
    	    	System.out.println("========== BUTTONS ==========");
    	    	for (WebElement b : buttons) {
    	    	    try {
    	    	        System.out.println(
    	    	                "[" + b.getText() + "]");

    	    	    } catch (Exception ignored) {
    	    	    }
    	    	}
    	    	
    	    	System.out.println("Current URL : " + driver.getCurrentUrl());
    	    	System.out.println("Title       : " + driver.getTitle());
    	    	
    	    	WebDriverWait wait =
    	    	        new WebDriverWait(driver, Duration.ofSeconds(20));

    	    	wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

    	    	browser.sleep(3000);
    	    	System.out.println("After Apply Click");
    	    	System.out.println(driver.getCurrentUrl());
    	    	System.out.println(driver.getTitle());
    	    	
    	    	WebElement apply = findApplyButton(driver);
    	    			
    	    	browser.switchToNewestTab();

//          WebElement apply = FormFiller.findButton(driver, "Apply");
			if (apply == null) {

				String html = driver.getPageSource();

				if (html.contains("Already Applied")) {

					return new ApplyResult(job.id(), true, "Already Applied");
				}

				System.out.println(html.substring(0, Math.min(html.length(), 3000)));

				return new ApplyResult(job.id(), false, "Apply button not found");
			}
            try {
                apply.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].click()", apply);
            }
            browser.sleep(3000);

            FormFiller.fillVisibleFields(driver, draft, profile);
            WebElement submit = FormFiller.findButton(driver, "Submit");
            if (submit == null) {
                submit = FormFiller.findButton(driver, "Send");
            }
            if (submit != null) {
            	try {

            	    submit.click();

            	} catch (Exception e) {

            	    ((JavascriptExecutor) driver)
            	            .executeScript("arguments[0].click()", submit);
            	}

            	browser.sleep(5000);

            	String page = driver.getPageSource();

            	if (page.contains("Application submitted")
            	        || page.contains("Successfully Applied")
            	        || page.contains("Already Applied")
            	        || page.contains("You have successfully applied")) {

            	    return new ApplyResult(
            	            job.id(),
            	            true,
            	            "Application Submitted Successfully");
            	}

            	return new ApplyResult(
            	        job.id(),
            	        false,
            	        "Submit clicked but success message not found");
            }
            return new ApplyResult(job.id(), false, "Fields filled but no confirm button found");
        } catch (Exception e) {
            log.warn("naukri submit failed for {}: {}", job.id(), e.getMessage());
            return new ApplyResult(job.id(), false, e.getMessage());
        }
    }
    
    private WebElement findApplyButton(WebDriver driver) {

        String xpath =
                "//button[contains(.,'Apply')]" +
                "|//button[contains(.,'Apply Now')]" +
                "|//button[contains(.,'Apply now')]" +
                "|//button[contains(.,'Already Applied')]" +
                "|//button[contains(.,'Apply on company site')]" +
                "|//a[contains(.,'Apply')]";

        List<WebElement> buttons = driver.findElements(By.xpath(xpath));

        for (WebElement button : buttons) {

            try {

                if (button.isDisplayed() && button.isEnabled()) {

                    System.out.println("Button Found : " + button.getText());

                    return button;
                }

            } catch (Exception ignored) {
            }
        }

        return null;
    }
}