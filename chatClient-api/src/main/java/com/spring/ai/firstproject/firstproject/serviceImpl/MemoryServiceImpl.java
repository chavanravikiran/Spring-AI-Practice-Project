package com.spring.ai.firstproject.firstproject.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.spring.ai.firstproject.firstproject.entity.Tutorial;
import com.spring.ai.firstproject.firstproject.service.ChatService;
import com.spring.ai.firstproject.firstproject.service.MemoryService;

import reactor.core.publisher.Flux;

@Service
public class MemoryServiceImpl implements MemoryService {

	public ChatClient chatClient;

	public MemoryServiceImpl(ChatClient.Builder chatClient) {
		this.chatClient = chatClient.build();
	}

    @Override
    public String memory(String conversationId, String query) {

        return chatClient.prompt()
                .user(query)
                .advisors(advisor -> advisor.param("conversationId", conversationId))
                .call()
                .content();
    }

    @Override
    public Flux<String> stream(String conversationId, String query) {

        return chatClient.prompt()
                .user(query)
                .advisors(advisor -> advisor.param("conversationId", conversationId))
                .stream()
                .content();
    }
}

