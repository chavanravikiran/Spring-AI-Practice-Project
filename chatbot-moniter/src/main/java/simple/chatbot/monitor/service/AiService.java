package simple.chatbot.monitor.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import simple.chatbot.monitor.dto.AiResponse;

@Service
public class AiService {

//	private final ChatClient chatClient;
//	
//	public AiService(ChatClient.Builder chatClient) {
//		this.chatClient = chatClient.build();
//	}
	
	@Autowired
	private ChatClient chatClient;

	public AiResponse askForAi(String query) {
		String response = chatClient.prompt(query).call().content();
		return new AiResponse(response,true);
	}

}
