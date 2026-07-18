package com.spring.ai.firstproject.firstproject.serviceImpl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import com.spring.ai.firstproject.firstproject.service.MemoryService;
import reactor.core.publisher.Flux;

@Service
public class MemoryServiceImpl implements MemoryService {

	private final ChatClient chatClient;

	public MemoryServiceImpl(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Override
	public String memory(String conversationId, String query) {

		return chatClient.prompt().user(query)
//                .advisors(advisor -> advisor.param("conversationId", conversationId))
				.advisors(advisor -> advisor.param("chat_memory_conversation_id", conversationId)).call().content();
	}

	@Override
	public Flux<String> stream(String conversationId, String query) {

		return chatClient.prompt().user(query)
				.advisors(advisor -> advisor.param("chat_memory_conversation_id", conversationId)).stream().content();
	}

	@Override
	public String memorySession(String userId,  String query) {
		return chatClient.prompt().user(query)
				.advisors(advisor -> advisor.param("chat_memory_conversation_id", userId)).call().content();
	}
}
