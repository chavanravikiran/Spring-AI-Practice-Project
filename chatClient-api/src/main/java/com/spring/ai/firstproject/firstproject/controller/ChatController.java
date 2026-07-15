package com.spring.ai.firstproject.firstproject.controller;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.ai.firstproject.firstproject.entity.Tutorial;
import com.spring.ai.firstproject.firstproject.service.ChatService;

@RestController
@RequestMapping("/")
public class ChatController {
	
////	When Single ChatClient
////	private ChatClient chatClient;
//	
////	When single chatModel Use
////	public ChatController(ChatClient.Builder builder) {
////		this.chatClient = builder.build();
////	}
//	
////	openAiChatClient
//	private ChatClient openAiChatClient;
//	
//	//ollamaAiChatClient
//	private ChatClient ollamaAiChatClient;
//
//	//when Multiple ChatClient/chatModel use openAiChatClient and OllamaAiChatClient
////	public ChatController(OpenAiChatModel openAiChatModel , OllamaChatModel ollamaChatModel) {
////		this.openAiChatClient = ChatClient.builder(openAiChatModel).build();
////		this.ollamaAiChatClient = ChatClient.builder(ollamaChatModel).build();
////	}
//	
//	
//	public ChatController(@Qualifier("openAiChatClient") ChatClient openAiChatClient, @Qualifier("ollamaChatClient") ChatClient ollamaChatClient) {
//		this.openAiChatClient = openAiChatClient;
//		this.ollamaAiChatClient = ollamaChatClient;
//	}
//	
//	@GetMapping("/chat")
//	public ResponseEntity<String> chat(@RequestParam(value = "q",required = true) String q){
//		System.out.println("-=---------->"+q);
//		
////		When Single ChatClient
////		var resultResponse = chatClient.prompt(q).call().content();
//		
////		When multiple ChatClient Used
//		var resultResponse = openAiChatClient.prompt(q).call().content();
//		return ResponseEntity.ok(resultResponse);
//		
//	}
	
	
	@Autowired
	public ChatService chatService;
	
	public ChatClient chatClient;

	public ChatController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}
	
	
	@GetMapping("/userAndSystemPrompt")
	public ResponseEntity<String> userAndSystemPrompt(@RequestParam(value = "query",required = true) String query){
		return ResponseEntity.ok(chatService.chat(query));
	}
	
	@GetMapping("/generatePromptInEntity")
	public ResponseEntity<Tutorial> generatePromptInEntity(@RequestParam(value = "query",required = true) String query){
		return ResponseEntity.ok(chatService.entityResponse(query));
	}

	@GetMapping("/generateListOfPromptInEntity")
	public ResponseEntity<List<Tutorial>> generateListOfPromptInEntity(@RequestParam(value = "query",required = true) String query){
		return ResponseEntity.ok(chatService.listOfEntityResponse(query));
	}
	
	@GetMapping("/promptTemplate")
	public ResponseEntity<String> promptTemplate(@RequestParam String query){
		return ResponseEntity.ok(chatService.promptTemplate(query));
	}
	
	@GetMapping("/dynamicQueryPrompt")
	public ResponseEntity<String> dynamicQueryPrompt(@RequestParam String query){
		return ResponseEntity.ok(chatService.dynamicQueryPrompt(query));
	}
	 
	@GetMapping("/fluentPrompt")
	public ResponseEntity<String> fluentPrompt(@RequestParam String query){
		return ResponseEntity.ok(chatService.fluentPrompt(query));
	}
}
