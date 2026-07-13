package com.spring.ai.firstproject.firstproject.serviceImpl;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.spring.ai.firstproject.firstproject.entity.Tutorial;
import com.spring.ai.firstproject.firstproject.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

	public ChatClient chatClient;

	public ChatServiceImpl(ChatClient.Builder chatClient) {
		this.chatClient = chatClient.build();
	}

	@Override
	public String chat(String query) {

		String content = 
				chatClient
				.prompt()// generate more specific prompt
				.user(query)
				.system("as an experts in cricket")
				.call()
				.content();

		System.out.println(content);
		return content;
	}

	@Override
	public Tutorial entityResponse(String query) {
		
		Tutorial tutorial = chatClient.prompt(query)
		.call()
		.entity(Tutorial.class);
		
		System.out.println(tutorial);
		return tutorial;
	}

	@Override
	public List<Tutorial> listOfEntityResponse(String query) {
		List<Tutorial> tutorials = 
				chatClient
				.prompt(query)
				.call()
				.entity(new ParameterizedTypeReference<List<Tutorial>>() {//when return data in multiple list then use ParameterizedTypeReference
				});

		System.out.println(tutorials);

		return tutorials;

	}

}
