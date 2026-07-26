package com.toolcalling.tool_calling.serviceImpl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.toolcalling.tool_calling.services.ChatService;
import com.toolcalling.tool_calling.tools.SimpleDateTimeTool;
import com.toolcalling.tool_calling.tools.WeatherTool;

@Service
public class ChatServiceImpl implements ChatService{

	public ChatClient chatClient;
	private final SimpleDateTimeTool simpleDateTimeTool;
	private final WeatherTool weatherTool;
	
	public ChatServiceImpl(ChatClient.Builder chatClient,SimpleDateTimeTool simpleDateTimeTool,WeatherTool weatherTool) {
		this.chatClient = chatClient.build();
		this.simpleDateTimeTool = simpleDateTimeTool;
		this.weatherTool = weatherTool;
	}
	
	@Override
	public String chat(String query) {
		return chatClient
				.prompt(query)
//				.tools(new SimpleDateTimeTool())
				.tools(simpleDateTimeTool,weatherTool)
				.call()
				.content();
	}

}
