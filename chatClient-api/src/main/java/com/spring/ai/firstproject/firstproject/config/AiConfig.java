package com.spring.ai.firstproject.firstproject.config;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

//	@Bean(name="openAiChatClient")
//	public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
//		return ChatClient.builder(chatModel).build();
//	}
//
//	@Bean(name="ollamaChatClient")
//	public ChatClient ollamaChatClient(OllamaChatModel chatModel) {
//		return ChatClient.builder(chatModel).build();
//	}

	@Bean
    public ChatClient chatClient(ChatClient.Builder builder,
                                 ChatMemory chatMemory) {

        MessageChatMemoryAdvisor memoryAdvisor =
                MessageChatMemoryAdvisor.builder(chatMemory)
                        .build();

        return builder
                .defaultAdvisors(
                        memoryAdvisor,
                        new SimpleLoggerAdvisor(),
                        new SafeGuardAdvisor(
                                List.of("hack", "virus", "malware")))
                .build();
    }

}
