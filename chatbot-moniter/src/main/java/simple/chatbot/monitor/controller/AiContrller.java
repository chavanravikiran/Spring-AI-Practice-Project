package simple.chatbot.monitor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import simple.chatbot.monitor.dto.AiResponse;
import simple.chatbot.monitor.service.AiService;

@RestController
@RequestMapping("/api/v1/ai")
public class AiContrller {

	private final AiService aiService;

	public AiContrller(AiService aiService) {
		this.aiService = aiService;
	}
 
	@PostMapping()
	public ResponseEntity<AiResponse> askForAi(@RequestParam("query") String query){
		AiResponse aiResponse = aiService.askForAi(query);
		return ResponseEntity.ok(aiResponse);
	}
	
}
