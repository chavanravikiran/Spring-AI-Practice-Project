package com.substring.helpdesk.servicesImpl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.substring.helpdesk.tools.EmailTool;
import com.substring.helpdesk.tools.TicketDatabaseTool;
import org.springframework.core.io.Resource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class AiService {

	private final ChatClient chatClient;
	
	private final TicketDatabaseTool ticketDatabaseTool;
	private final EmailTool emailTool;
	
	@Value("classpath:/helpdesk-system.st")
	private Resource systemPromptResource;
	
	public String getReponseFromAssistant(String query,String conversationId) {
		
		//basic call to LLM
		return this.chatClient
				.prompt()
				//multiple tools calling
				.tools(ticketDatabaseTool,emailTool)
				.system(systemPromptResource)
				.user(query)
				.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
				.call()
				.content();
		
		
	}
	
	public Flux<String> streamResponseFromAssistant(String query,String conversationId){
		return this.chatClient
				.prompt()
				.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
				//multiple tools calling
				.tools(ticketDatabaseTool,emailTool)
				.system(systemPromptResource)
				.user(query)
				.stream()
				.content();
	}
}
