package spring.google.gemini.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.google.gemini.service.AiService;

@RestController
@RequestMapping("/ai")
public class AiController {

	private final AiService aiService;

	public AiController(AiService aiService) {
		this.aiService = aiService;
	}
	
	@GetMapping
	public String getResponseFromAI(@RequestParam("prompt") String prompt) {
		return aiService.getResponeFromAI(prompt);
	}
}
