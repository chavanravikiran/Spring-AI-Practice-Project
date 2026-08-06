package com.example.jobagent.browser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
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
//				if (l.contains("degree"))
//				    return profile.degree();

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
				
				if(l.contains("marathi"))
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
				
				if(l.contains("current ctc"))
				    return profile.currentCTC();

				if(l.contains("expected ctc"))
				    return profile.expectedCTC();

				if(l.contains("fixed salary"))
				    return profile.currentSalary();

				if(l.contains("variable salary"))
				    return "0";

				if(l.contains("monthly salary"))
				    return String.valueOf(Integer.parseInt(profile.currentSalary())/12);

				if(l.contains("expected salary"))
				    return profile.expectedSalary();

				if(l.contains("salary negotiable"))
				    return "Yes";
				
				if(l.contains("serving notice"))
				    return "No";

				if(l.contains("last working"))
				    return "NA";

				if(l.contains("immediate"))
				    return "No";

				if(l.contains("30 days"))
				    return "Yes";
				
				if(l.contains("highest qualification"))
				    return profile.degree();

				if(l.contains("degree"))
				    return profile.degree();

//				if(l.contains("college"))
//				    return profile.college();
//
//				if(l.contains("university"))
//				    return profile.college();
//
//					duplicate
//				if(l.contains("passing year"))
//				    return profile.graduationYear();

				if(l.contains("graduation"))
				    return profile.graduationYear();
				
//					duplicate
//				if(l.contains("cgpa"))
//				    return profile.cgpa();

				if(l.contains("percentage"))
				    return "80";
				
				if(l.contains("primary skill"))
				    return String.join(", ",profile.skills());

				if(l.contains("secondary"))
				    return String.join(", ",profile.skills());

				if(l.contains("technical"))
				    return String.join(", ",profile.skills());

				if(l.contains("programming"))
				    return "Java";

				if(l.contains("framework"))
				    return "Spring Boot";

				if(l.contains("database"))
				    return "PostgreSQL, Oracle";

				if(l.contains("cloud"))
				    return "AWS";

				if(l.contains("tools"))
				    return "Git, Docker, Jenkins";

				if(l.contains("version control"))
				    return "Git";
				
				if(l.contains("resume")
				        || l.contains("upload resume")
				        || l.contains("cv"))
				    return profile.resumePath();
				
				if(l.contains("authorized"))
				    return "Yes";

				if(l.contains("sponsorship"))
				    return "No";

				if(l.contains("visa"))
				    return profile.visaStatus();

				if(l.contains("passport"))
				    return "Yes";

				if(l.contains("nationality"))
				    return "Indian";

				if(l.contains("citizenship"))
				    return profile.workAuthorization();
				
				if(l.contains("current location"))
				    return profile.location();

				if(l.contains("preferred location"))
				    return profile.location();

				if(l.contains("preferred city"))
				    return profile.city();

				if(l.contains("relocate"))
				    return "Yes";

				if(l.contains("work from office"))
				    return "Yes";

				if(l.contains("remote"))
				    return "Yes";

				if(l.contains("hybrid"))
				    return "Yes";
				
				if(l.contains("full time"))
				    return "Yes";

				if(l.contains("part time"))
				    return "No";

				if(l.contains("contract"))
				    return "Yes";

				if(l.contains("permanent"))
				    return "Yes";

				if(l.contains("internship"))
				    return "No";

				if(l.contains("freelance"))
				    return "No";

				if(l.contains("night shift"))
				    return "No";

				if(l.contains("weekend"))
				    return "Yes";

				if(l.contains("travel"))
				    return "Yes";
				
				if(l.contains("why join"))
				    return profile.whyJoin();

				if(l.contains("why should we hire"))
				    return profile.whyHire();

				if(l.contains("reason for leaving"))
				    return "Looking for better growth opportunities.";

				if(l.contains("describe yourself"))
				    return draft.summary();

				if(l.contains("tell us about yourself"))
				    return draft.summary();

				if(l.contains("career objective"))
				    return profile.careerGoal();

				if(l.contains("professional summary"))
				    return draft.summary();

				if(l.contains("strength"))
				    return profile.strength();

				if(l.contains("weakness"))
				    return profile.weakness();

				if(l.contains("achievement"))
				    return profile.achievement();

				if(l.contains("leadership"))
				    return profile.leadership();

				if(l.contains("conflict"))
				    return "I resolve conflicts through communication and collaboration.";

				if(l.contains("teamwork"))
				    return profile.teamWork();

				if(l.contains("career goal"))
				    return profile.careerGoal();

				if(l.contains("motivation"))
				    return profile.motivation();
				
				if(l.contains("recent project"))
				    return profile.experience().get(0);

				if(l.contains("current project"))
				    return profile.experience().get(0);

				if(l.contains("role"))
				    return profile.currentDesignation();

				if(l.contains("responsibilities"))
				    return "Backend API development, Microservices, Database Design.";

				if(l.contains("technologies"))
				    return String.join(", ",profile.skills());

				if(l.contains("team size"))
				    return "8";

				if(l.contains("project duration"))
				    return "2 Years";

				if(l.contains("challenge"))
				    return profile.achievement();
				
				if(l.contains("aws certified"))
				    return "No";

				if(l.contains("oracle certified"))
				    return "No";

				if(l.contains("azure"))
				    return "No";

				if(l.contains("java certification"))
				    return "Yes";

				if(l.contains("spring certification"))
				    return "Yes";
				
				if(l.contains("criminal"))
				    return "No";

				if(l.contains("background verification"))
				    return "Yes";

				if(l.contains("bond"))
				    return "No";

				if(l.contains("buyout"))
				    return "No";

				if(l.contains("previously worked"))
				    return "No";

				if(l.contains("relatives"))
				    return "No";
				
				if(l.contains("comfortable"))
				    return "Yes";

				if(l.contains("agile"))
				    return "Yes";

				if(l.contains("scrum"))
				    return "Yes";

				if(l.contains("internet"))
				    return "Yes";

				if(l.contains("laptop"))
				    return "Yes";

				if(l.contains("passport"))
				    return "Yes";
				
				
				
		// ----------------------------
		// Summary
		// ----------------------------
			
				
		// ----------------------------
		// For Naukri
		// ----------------------------
				
				if (l.contains("current company")
				        || l.contains("current employer")
				        || l.contains("current organization")
				        || l.equals("company"))
				    return profile.currentCompany();

				if (l.contains("designation")
				        || l.contains("current role")
				        || l.contains("job title"))
				    return profile.currentDesignation();

				if (l.contains("employment type"))
				    return "Full Time";

				if (l.contains("industry"))
				    return "Information Technology";

				if (l.contains("department"))
				    return "Software Development";
				
				if (l.contains("backend"))
				    return String.valueOf(profile.relevantExperience());
				
				if(l.contains("total experience"))
				    return String.valueOf(profile.totalExperience());

				if(l.contains("relevant experience"))
				    return String.valueOf(profile.relevantExperience());

				if(l.contains("country"))
				    return profile.country();

				if(l.contains("state"))
				    return profile.state();

				if(l.contains("zip"))
				    return profile.zipCode();

				if(l.contains("postal"))
				    return profile.zipCode();

				if(l.contains("dob"))
				    return profile.dateOfBirth();

				if(l.contains("gender"))
				    return profile.gender();
				
				if (l.contains("experience"))
				{
				    if(profile.skillExperience()!=null){

				        for(Map.Entry<String,Integer> e : profile.skillExperience().entrySet()){

				            if(l.contains(e.getKey().toLowerCase()))
				                return String.valueOf(e.getValue());
				        }
				    }

				    return String.valueOf(profile.totalExperience());
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
    
	public static void fillForm(WebDriver driver, ApplicationDraft draft, CandidateProfile profile) {

		fillTextFields(driver, draft, profile);

		fillTextAreas(driver, draft, profile);

		fillDropdowns(driver, draft, profile);

		fillRadioButtons(driver, draft, profile);

		fillCheckboxes(driver);

		uploadResume(driver, profile);

		clickNextUntilSubmit(driver, draft, profile);
	}

	private static void fillDropdowns(WebDriver driver, ApplicationDraft draft, CandidateProfile profile) {

		List<WebElement> selects = driver.findElements(By.tagName("select"));
		for (WebElement s : selects) {
			try {
				Select select = new Select(s);
				String label = labelOf(s);
				String value = decideContent(label, "select", draft, profile);
				select.selectByVisibleText(value);
			} catch (Exception ignored) {
			}
		}
	}
	
	private static void fillRadioButtons(WebDriver driver, ApplicationDraft draft, CandidateProfile profile) {
		List<WebElement> radios = driver.findElements(By.cssSelector("input[type='radio']"));
		for (WebElement r : radios) {
			try {
				String label = r.getAttribute("value");
				if (label == null)
					continue;
				label = label.toLowerCase();
				if (label.contains("yes")) {
					if (!r.isSelected())
						r.click();
				}
			} catch (Exception ignored) {
			}
		}
	}
	
	private static void fillCheckboxes(WebDriver driver) {
		List<WebElement> checks = driver.findElements(By.cssSelector("input[type='checkbox']"));
		for (WebElement c : checks) {
			try {
				if (!c.isSelected())
					c.click();
			} catch (Exception ignored) {
			}
		}
	}
	
	//"resumePath":"C:\\Resume\\Ravikiran.pdf"
//	private static void uploadResume(WebDriver driver, CandidateProfile profile) {
//		try {
//			System.out.println(driver.findElements(By.cssSelector("input[type='file']")).size());
//			List<WebElement> uploads = driver.findElements(By.cssSelector("input[type='file']"));
//
//			if (uploads.isEmpty()) {
//				return;
//			}
//
//			File resume = new File(profile.resumePath());
//
//			if (!resume.exists()) {
//				logger.warn("Resume not found : {}", resume.getAbsolutePath());
//				return;
//			}
//
//			uploads.get(0).sendKeys(resume.getAbsolutePath());
//			logger.info("Resume uploaded successfully.");
//		} catch (Exception e) {
//			logger.warn("Resume upload failed", e);
//		}
//	}
	private static void uploadResume(WebDriver driver, CandidateProfile profile) {
		try {
			// Step 1 : Click Upload Resume button if present
			List<WebElement> uploadBtns = driver.findElements(By.xpath("//button[contains(.,'Upload Resume')]"));

			if (!uploadBtns.isEmpty()) {
				uploadBtns.get(0).click();
				Thread.sleep(1500);
			}

			// Step 2 : locate hidden file input
			List<WebElement> inputs = driver.findElements(By.cssSelector("input[type='file']"));
//			List<WebElement> inputs = driver.findElements(By.xpath("//input[@type='file']"));

			if (inputs.isEmpty()) {
				logger.info("No file input found.");
				return;
			}

			File resume = new File(profile.resumePath());
			inputs.get(0).sendKeys(resume.getAbsolutePath());
			logger.info("Resume uploaded.");
			Thread.sleep(3000);
		} catch (Exception e) {
			logger.error("Resume upload failed", e);
		}
	}
	
	private static void clickNextUntilSubmit(WebDriver driver, ApplicationDraft draft, CandidateProfile profile) {
		while (true) {
			fillTextFields(driver, draft, profile);
			fillDropdowns(driver, draft, profile);
			fillRadioButtons(driver, draft, profile);
			fillCheckboxes(driver);

			WebElement next = findButton(driver, "Next");
			
			if (next != null) {
				next.click();
				pause();
				continue;
			}

			WebElement save = findButton(driver, "Save");
			if (save != null) {
				save.click();
				pause();
				continue;
			}
			break;
		}
	}
	
	private static void fillTextFields(WebDriver driver, ApplicationDraft draft, CandidateProfile profile) {
		List<WebElement> inputs = driver.findElements(By.tagName("input"));

		for (WebElement el : inputs) {
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
				String answer = decideContent(label, "input", draft, profile);

				if (answer == null || answer.isBlank())
					continue;

				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);

				Thread.sleep(400);

				el.clear();
				el.sendKeys(answer);
				logger.info("INPUT : {} -> {}", label, answer);
				pause();
			} catch (Exception ex) {
				logger.warn("Unable to fill input {}", labelOf(el));
			}
		}
	}
	
	private static void fillTextAreas(WebDriver driver, ApplicationDraft draft, CandidateProfile profile) {
		List<WebElement> textareas = driver.findElements(By.tagName("textarea"));

		for (WebElement el : textareas) {
			try {
				if (!el.isDisplayed())
					continue;

				String current = el.getAttribute("value");

				if (current != null && !current.isBlank())
					continue;

				String label = labelOf(el);
				String answer = decideContent(label, "textarea", draft, profile);

				if (answer == null || answer.isBlank())
					continue;

				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);

				Thread.sleep(400);
				el.clear();
				el.sendKeys(answer);
				logger.info("TEXTAREA : {} -> {}", label, answer);
				pause();
			} catch (Exception ex) {
				logger.warn("Unable to fill textarea {}", labelOf(el));
			}
		}
	}
	
}