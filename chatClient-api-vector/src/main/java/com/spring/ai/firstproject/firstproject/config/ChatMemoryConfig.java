package com.spring.ai.firstproject.firstproject.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
//import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {

//    @Bean
//    public ChatMemory chatMemory() {
//        return new InMemoryChatMemory();
//    }

//	@Bean
//	public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
//		return MessageWindowChatMemory.builder()
//				.chatMemoryRepository(jdbcChatMemoryRepository)
//				.maxMessages(10)
//				.build();
//	}
	
	@Bean
	public ChatMemory chatMemory() {
		InMemoryChatMemoryRepository chatMemoryRepository = new InMemoryChatMemoryRepository();
		return MessageWindowChatMemory.builder().maxMessages(10).chatMemoryRepository(chatMemoryRepository).build();
	}
	
	@Bean
	public ChatClient chatClient(ChatClient.Builder builder,ChatMemory chatMemory) {
		MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
		return builder
				.defaultAdvisors(messageChatMemoryAdvisor,new SimpleLoggerAdvisor())
				.build();
	}
}