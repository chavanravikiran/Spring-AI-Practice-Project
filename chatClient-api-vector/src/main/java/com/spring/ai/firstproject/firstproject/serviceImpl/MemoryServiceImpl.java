package com.spring.ai.firstproject.firstproject.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import com.spring.ai.firstproject.firstproject.service.MemoryService;
import reactor.core.publisher.Flux;

@Service
public class MemoryServiceImpl implements MemoryService {

	private final ChatClient chatClient;

//	@Autowired
	private VectorStore vectorStore;

	public MemoryServiceImpl(ChatClient chatClient,VectorStore vectorStore) {
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;
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

	//Dump Dummy Data in Database
	@Override
	public void saveData(List<String> list) {
		List<Document> document = list.stream().map(Document::new).collect(Collectors.toList());
		this.vectorStore.add(document);
	}

	
	
}
