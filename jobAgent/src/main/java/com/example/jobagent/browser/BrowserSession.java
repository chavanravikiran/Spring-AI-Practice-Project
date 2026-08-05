package com.example.jobagent.browser;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;
import com.example.jobagent.config.AgentProperties;

@Component
public class BrowserSession implements AutoCloseable {

    private final AgentProperties props;
    private WebDriver driver;

    public BrowserSession(AgentProperties props) {
        this.props = props;
    }

    public synchronized WebDriver driver() {
        if (driver == null) {

        	ChromeOptions options = new ChromeOptions();

        	options.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");

        	options.addArguments("--user-data-dir=C:\\ChromeProfile");
        	options.addArguments("--profile-directory=Default");

        	// DO NOT USE HEADLESS
        	// options.addArguments("--headless=new");

        	options.addArguments("--start-maximized");
        	options.addArguments("--remote-allow-origins=*");
        	options.addArguments("--disable-blink-features=AutomationControlled");
        	options.addArguments("--disable-infobars");

//        	WebDriver driver = new ChromeDriver(options);
            options.setExperimentalOption(
                    "excludeSwitches",
                    List.of("enable-automation"));

            options.setExperimentalOption(
                    "useAutomationExtension",
                    false);

            options.addArguments("--disable-blink-features=AutomationControlled");
            driver = new ChromeDriver(options);
            ((JavascriptExecutor) driver).executeScript(
            		"Object.defineProperty(navigator,'webdriver',{get:()=>undefined})");
        }
        return driver;
    }

    public void open(String url) {
        driver().get(url);
    }
    
    public void openInNewTab(String url) {
        JavascriptExecutor js = (JavascriptExecutor) driver();
        js.executeScript("window.open();");
        List<String> tabs = new ArrayList<>(driver().getWindowHandles());
        driver().switchTo().window(tabs.get(tabs.size() - 1));
        System.out.println("Opening URL = " + url);
        driver().get(url);
        System.out.println("After get() = " + driver().getCurrentUrl());
    }
    public void switchToNewestTab() {

        List<String> tabs = new ArrayList<>(driver().getWindowHandles());

        if (!tabs.isEmpty()) {
            driver().switchTo().window(tabs.get(tabs.size() - 1));
        }
    }
    
    public WebElement waitFor(By by, long timeoutSeconds) {
        try {
            return new WebDriverWait(driver(), Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (TimeoutException e) {
            throw e;
        }
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void randomDelay() {
        long min = props.search.delayMinMs;
        long max = props.search.delayMaxMs;
        long lower = min > 0 ? min : 1500;
        long span = Math.max(1, max - lower + 1);
        sleep(lower + ThreadLocalRandom.current().nextLong(span));
    }

    public synchronized void close() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
            driver = null;
        }
    }
}