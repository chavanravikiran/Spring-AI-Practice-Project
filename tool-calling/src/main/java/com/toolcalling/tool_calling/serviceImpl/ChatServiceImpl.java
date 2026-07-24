package com.toolcalling.tool_calling.serviceImpl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.toolcalling.tool_calling.services.ChatService;
import com.toolcalling.tool_calling.tools.SimpleDateTimeTool;

@Service
public class ChatServiceImpl implements ChatService{

	public ChatClient chatClient;
	private final SimpleDateTimeTool simpleDateTimeTool;

	public ChatServiceImpl(ChatClient.Builder chatClient,SimpleDateTimeTool simpleDateTimeTool) {
		this.chatClient = chatClient.build();
		this.simpleDateTimeTool = simpleDateTimeTool;
	}
	
	@Override
	public String chat(String query) {
		return chatClient
				.prompt(query)
//				.tools(new SimpleDateTimeTool())
				.tools(simpleDateTimeTool)
				.call()
				.content();
	}

}
