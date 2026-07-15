package com.spring.ai.firstproject.firstproject.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.spring.ai.firstproject.firstproject.entity.Tutorial;
import com.spring.ai.firstproject.firstproject.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService {

	public ChatClient chatClient;

	public ChatServiceImpl(ChatClient.Builder chatClient) {
		this.chatClient = chatClient.build();
	}

	@Value("classpath:/prompts/userPrompts-message.st")
	private Resource userPrompt;

	@Override
	public String chat(String query) {

		String content = chatClient
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

		Tutorial tutorial = chatClient
				.prompt(query)
				.call()
				.entity(Tutorial.class);

		System.out.println(tutorial);
		return tutorial;
	}

	@Override
	public List<Tutorial> listOfEntityResponse(String query) {
		List<Tutorial> tutorials = chatClient
				.prompt(query)
				.call()
				.entity(new ParameterizedTypeReference<List<Tutorial>>() {});// when return data in multiple list then use
																			// ParameterizedTypeReference
				

		System.out.println(tutorials);

		return tutorials;

	}

	@Override
	public String promptTemplate(String query) {

		Prompt prompt = new Prompt(query);

		String result = chatClient
				.prompt(query)
				.user(query)
				.system("Give me in dotNet")// provide more specific prompt
				.call()
				.content();

		return result;
	}

	@Override
	public String dynamicQueryPrompt(String query) {
		String dynamicVar = "As an expert in coding and programing.Always write program in java. now reply for this question: {keyQuery}";

		String result = chatClient
				.prompt()
				.user(x -> x.text(dynamicVar).param("keyQuery", query))
				.call()
				.content();

		return result;
	}

	@Override
	public String fluentPrompt(String query) {
		String result = chatClient
				.prompt()
				.user(x -> x.text(userPrompt).params(Map.of("name", "Ravikiran", "technology",
						"Spring AI", "experience", 3, "question", "Explain PromptTemplate")))
				.call()
				.content();
		return result;
	}

}
