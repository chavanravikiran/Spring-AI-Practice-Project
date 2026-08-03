package com.example.jobagent.browser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jobagent.model.ApplicationDraft;
import com.example.jobagent.model.CandidateProfile;

public final class FormFiller {

    private static final Set<String> SKIP_TYPES = Set.of("hidden", "submit", "button", "checkbox", "radio", "file");

    private static final Logger logger = LoggerFactory.getLogger(FormFiller.class);
    private FormFiller() {
    }

    public static void fillVisibleFields(WebDriver driver, ApplicationDraft draft, CandidateProfile profile) {
        List<WebElement> controls = new ArrayList<>(driver.findElements(By.tagName("input")));
        controls.addAll(driver.findElements(By.tagName("textarea")));
        for (WebElement el : controls) {
            try {
                if (!el.isDisplayed()) {
                    continue;
                }
                String type = el.getAttribute("type");
                if (type != null && SKIP_TYPES.contains(type.toLowerCase())) {
                    continue;
                }
                String current = el.getAttribute("value");
                if (current != null && !current.isBlank()) {
                    continue;
                }
                String content = decideContent(labelOf(el), el.getTagName(), draft, profile);
                if (content == null || content.isBlank()) {
                    continue;
                }
                el.click();
                el.sendKeys(content);
                pause();
            } catch (Exception ignored) {
            }
        }
    }

    public static WebElement findButton(WebDriver driver, String text) {
        List<WebElement> elements = driver.findElements(
                By.xpath("//button[contains(normalize-space(.), '" + text + "')]"));
        logger.info("----->---> kahi tr yeudet {}",elements);
        System.out.println("----->---> kahi tr yeudet "+elements);
        for (WebElement el : elements) {
        	logger.info("----->---> el kahi tr yeudet {} ",el);
        	System.out.println("----->---> el kahi tr yeudet "+el);
            if (el.isDisplayed()) {
                return el;
            }
        }
        return null;
    }

    private static String labelOf(WebElement el) {
        String[] attrs = {"aria-label", "name", "placeholder", "id", "title"};
        for (String attr : attrs) {
            String value = el.getAttribute(attr);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private static String decideContent(String label, String tag, ApplicationDraft draft, CandidateProfile profile) {
        String l = label.toLowerCase();
        if (l.contains("email")) {
            return profile.email();
        }
        if (l.contains("phone")) {
            return profile.phone();
        }
        if (l.contains("first") || l.contains("last") || l.contains("name")) {
            return profile.name();
        }
        if (l.contains("location") || l.contains("city") || l.contains("address")) {
            return profile.location();
        }
        if (l.contains("linkedin")) {
            return profile.linkedin();
        }
        if (l.contains("portfolio") || l.contains("website") || l.contains("salary")
                || l.contains("visa") || l.contains("sponsor")) {
            return null;
        }
        if (l.contains("cover")) {
            return draft.coverLetter();
        }
        return draft.summary();
    }

    private static void pause() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}