package com.example.jobagent.browser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

				if (!el.isDisplayed())
					continue;

				String type = el.getAttribute("type");

				if (type != null && SKIP_TYPES.contains(type.toLowerCase()))
					continue;

				String current = el.getAttribute("value");

				if (current != null && !current.isBlank())
					continue;

				String label = labelOf(el);

				String content = decideContent(label, el.getTagName(), draft, profile);

				if (content == null || content.isBlank())
					continue;

				el.click();

				el.clear();

				el.sendKeys(content);

				logger.info("{} -> {}", label, content);

				pause();

			} catch (Exception ex) {

				logger.warn("Unable to fill field {}", labelOf(el));

			}

		}
	}

    public static WebElement findButton(WebDriver driver, String text) {

        String xpath =
            "//button[contains(translate(normalize-space(.),"
            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
            + "'abcdefghijklmnopqrstuvwxyz'),'"
            + text.toLowerCase() + "')]" +

            "|//a[contains(translate(normalize-space(.),"
            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
            + "'abcdefghijklmnopqrstuvwxyz'),'"
            + text.toLowerCase() + "')]" +

            "|//*[@role='button' and contains(translate(normalize-space(.),"
            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
            + "'abcdefghijklmnopqrstuvwxyz'),'"
            + text.toLowerCase() + "')]";

        List<WebElement> buttons =
                driver.findElements(By.xpath(xpath));

        for(WebElement b : buttons){

            if(b.isDisplayed() && b.isEnabled())
                return b;
        }

        return null;
    }
    public static WebElement findEasyApply(WebDriver driver) {

        List<WebElement> buttons = driver.findElements(By.xpath(
            "//button[" +
            "contains(.,'Easy Apply')" +
            " or contains(@aria-label,'Easy Apply')" +
            " or contains(@aria-label,'Apply')" +
            "]"));

        for (WebElement b : buttons) {
            try {
                if (b.isDisplayed() && b.isEnabled()) {
                    return b;
                }
            } catch (Exception ignored) {
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
		
		// ----------------------------
		// Email
		// ----------------------------
				if (l.contains("email"))
					return profile.email();
		
		// ----------------------------
		// Phone
		// ----------------------------
				if (l.contains("phone"))
					return profile.phone();
		
		// ----------------------------
		// Name
		// ----------------------------
				if (l.contains("first") || l.contains("last") || l.equals("name") || l.contains("full name"))
					return profile.name();
		
		// ----------------------------
		// Location
		// ----------------------------
				if (l.contains("location") || l.contains("city") || l.contains("address"))
					return profile.location();
		
		// ----------------------------
		// Years of Experience
		// ----------------------------
				if (l.contains("how many years") || l.contains("years of experience") || l.contains("experience with")
						|| l.contains("experience in")) {
		
					return "5";
				}
				
		// ----------------------------
		// Years of Experience
		// ----------------------------				
			if (l.contains("years")) {

			    if (profile.skillExperience() != null) {

			        for (Map.Entry<String, Integer> skill : profile.skillExperience().entrySet()) {

			            if (l.contains(skill.getKey().toLowerCase())) {
			                return String.valueOf(skill.getValue());
			            }
			        }
			    }

			    return "5";
			}
		// ----------------------------
		// Notice Period
		// ----------------------------
				if (l.contains("notice"))
					return "30";
		
		// ----------------------------
		// Notice Period
		// ----------------------------
				if (l.contains("joining"))
				    return profile.noticePeriod();

				if (l.contains("available to join"))
				    return profile.noticePeriod();

				if (l.contains("joining date"))
				    return profile.noticePeriod();
		// ----------------------------
		// Current CTC
		// ----------------------------
				if (l.contains("current ctc"))
					return "1000000";
		
		// ----------------------------
		// Expected CTC
		// ----------------------------
				if (l.contains("expected ctc"))
					return "1200000";
		
		// ----------------------------
		// Salary
		// ----------------------------
				if (l.contains("salary"))
					return "1200000";
		
		// ----------------------------
		// Visa
		// ----------------------------
				if (l.contains("visa"))
					return "No";
		
		// ----------------------------
		// Sponsorship
		// ----------------------------
				if (l.contains("sponsor"))
					return "No";
		
		// ----------------------------
		// Cover Letter
		// ----------------------------
				if (l.contains("cover"))
					return draft.coverLetter();
		
		// ----------------------------
		// Social Profiles
		// ----------------------------
		
				if (l.contains("linkedin"))
				    return profile.linkedin();

				if (l.contains("github"))
				    return profile.github();

				if (l.contains("portfolio"))
				    return profile.portfolio();

				if (l.contains("website"))
				    return profile.website();

				if (l.contains("stackoverflow"))
				    return "NA";	
			
			// ----------------------------
			// Skill Experience
			// ----------------------------	
				
				if (l.contains("java"))
				    return "5";

				if (l.contains("spring"))
				    return "5";

				if (l.contains("spring boot"))
				    return "5";

				if (l.contains("hibernate"))
				    return "5";

				if (l.contains("microservices"))
				    return "4";

				if (l.contains("sql"))
				    return "5";

				if (l.contains("mysql"))
				    return "2";

				if (l.contains("postgres"))
				    return "4";

				if (l.contains("aws"))
				    return "1";

				if (l.contains("docker"))
				    return "1";

				if (l.contains("kubernetes"))
				    return "0";

				if (l.contains("angular"))
				    return "3";

				if (l.contains("react"))
				    return "0";

				if (l.contains("javascript"))
				    return "1";

				if (l.contains("git"))
				    return "4";

				if (l.contains("jenkins"))
				    return "2";
		// ----------------------------
		// Education
		// ----------------------------
				if (l.contains("degree"))
				    return profile.degree();

				if (l.contains("qualification"))
				    return profile.degree();

				if (l.contains("college"))
				    return profile.college();

				if (l.contains("university"))
				    return profile.college();

				if (l.contains("graduation"))
				    return profile.graduationYear();

				if (l.contains("cgpa"))
				    return profile.cgpa();
				
		// ----------------------------
		// Summary
		// ----------------------------
				if (l.contains("resume"))
				    return profile.resumePath();

				if (l.contains("cv"))
				    return profile.resumePath();
		// ----------------------------
		// Professional Summary
		// ----------------------------
				
				if (l.contains("summary"))
				    return draft.summary();

				if (l.contains("professional summary"))
				    return draft.summary();

				if (l.contains("about yourself"))
				    return draft.summary();

				if (l.contains("introduce yourself"))
				    return draft.summary();

				if (l.contains("tell us about yourself"))
				    return draft.summary();
		// ----------------------------
		// Relocation
		// ----------------------------
				if (l.contains("relocate"))
				    return "Yes";

				if (l.contains("willing to relocate"))
				    return "Yes";

				if (l.contains("remote"))
				    return "Yes";

				if (l.contains("hybrid"))
				    return "Yes";

				if (l.contains("onsite"))
				    return "Yes";
		// ----------------------------
		// Employment Type
		// ----------------------------
				if (l.contains("full time"))
				    return "Yes";

				if (l.contains("contract"))
				    return "Yes";

				if (l.contains("internship"))
				    return "No";

				if (l.contains("part time"))
				    return "No";
		// ----------------------------
		// Languages
		// ----------------------------
				if (l.contains("language"))
				    return profile.languages();

				if (l.contains("english"))
				    return "Professional";

				if (l.contains("hindi"))
				    return "Native";
		// ----------------------------
		// Diversity Questions
		// ----------------------------
				if (l.contains("veteran"))
				    return "No";

				if (l.contains("disability"))
				    return "No";

				if (l.contains("ethnicity"))
				    return "Prefer not to say";

				if (l.contains("race"))
				    return "Prefer not to say";
		// ----------------------------
		// Criminal Background
		// ----------------------------
				if (l.contains("criminal"))
				    return "No";

				if (l.contains("convicted"))
				    return "No";

				if (l.contains("felony"))
				    return "No";

				if (l.contains("background check"))
				    return "Yes";
		// ----------------------------
		// Miscellaneous Questions
		// ----------------------------
				if (l.contains("why do you want"))
				    return profile.whyJoin();

				if (l.contains("why should we hire"))
				    return profile.whyHire();

				if (l.contains("greatest strength"))
				    return profile.strength();

				if (l.contains("greatest weakness"))
				    return profile.weakness();

				if (l.contains("achievement"))
				    return profile.achievement();

				if (l.contains("leadership"))
				    return profile.leadership();

				if (l.contains("team"))
				    return profile.teamWork();

				if (l.contains("motivation"))
				    return profile.motivation();

				if (l.contains("career goal"))
				    return profile.careerGoal();
		// ----------------------------
		// Common Yes/No Screening Questions
		// ----------------------------
				if (l.contains("willing to relocate"))
				    return "Yes";

				if (l.contains("eligible to work"))
				    return "Yes";

				if (l.contains("legally authorized"))
				    return "Yes";

				if (l.contains("require visa sponsorship"))
				    return "No";

				if (l.contains("have you worked"))
				    return "No";

				if (l.contains("security clearance"))
				    return "No";

				if (l.contains("non-compete"))
				    return "No";

				if (l.contains("travel"))
				    return "Yes";

				if (l.contains("weekends"))
				    return "Yes";

				if (l.contains("shift"))
				    return "Yes";

				if (l.contains("overtime"))
				    return "Yes";
		// ----------------------------
		// Summary
		// ----------------------------
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