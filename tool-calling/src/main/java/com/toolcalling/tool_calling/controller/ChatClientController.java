package com.toolcalling.tool_calling.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toolcalling.tool_calling.services.ChatService;
import com.toolcalling.tool_calling.tools.SimpleDateTimeTool;

@RestController
@RequestMapping("/")
public class ChatClientController {

	private ChatService chatService;
	
	public ChatClientController(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@GetMapping("/chat-tool")
	public String chat(@RequestParam(value = "query") String query) {
		return chatService.chat(query);
	}
	
	@GetMapping("/test")
	public String test() {
	    return SimpleDateTimeTool.getCurrentDateTime();
	}
	
}
