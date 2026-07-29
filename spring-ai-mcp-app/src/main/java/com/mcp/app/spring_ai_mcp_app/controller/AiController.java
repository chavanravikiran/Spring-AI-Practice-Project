package com.mcp.app.spring_ai_mcp_app.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AiController {

	private final ChatClient chatClient;

	public AiController(ChatClient.Builder chatClient,ToolCallbackProvider toolCallbackProvider) {
		this.chatClient = chatClient
				.defaultToolCallbacks(toolCallbackProvider)
				.defaultAdvisors(new SimpleLoggerAdvisor())
				.build();
	}
	
	@GetMapping("/mcp")
	public ResponseEntity<String> getAiResponse(@RequestParam("query") String query) {
		String result = chatClient.prompt(query)
		.call()
		.content();
		
		return ResponseEntity.ok(result);
	}
}
