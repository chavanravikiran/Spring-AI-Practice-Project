package com.toolcalling.tool_calling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toolcalling.tool_calling.services.ChatService;
import com.toolcalling.tool_calling.tools.SimpleDateTimeTool;
import com.toolcalling.tool_calling.tools.WeatherTool;

@RestController
@RequestMapping("/")
public class ChatClientController {

	private ChatService chatService;
	
	@Autowired
	private WeatherTool weatherTool;
	
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
	
	@GetMapping("/weatherToolCalling")
	public String weatherTool(@RequestParam(value = "query") String query) {
//	    return  weatherTool.getWeather(city);
		return chatService.chat(query);
	}
	
	
}
