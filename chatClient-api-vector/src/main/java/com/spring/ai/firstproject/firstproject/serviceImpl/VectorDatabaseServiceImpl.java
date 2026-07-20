package com.spring.ai.firstproject.firstproject.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.spring.ai.firstproject.firstproject.service.VectorDatabaseService;

@Service
public class VectorDatabaseServiceImpl implements VectorDatabaseService{
	
	private Logger logger = LoggerFactory.getLogger(VectorDatabaseServiceImpl.class);
	
	private final ChatClient chatClient;

	private VectorStore vectorStore;

	public VectorDatabaseServiceImpl(ChatClient chatClient,VectorStore vectorStore) {
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;
	}

	@Value("classpath:/prompts/SystemPrompts-message.st")
	private Resource sysetmMessage;
	
	@Value("classpath:/prompts/UserPrompts-message.st")
	private Resource userMessage;
	
	@Override
	public String chatTemplate(String userId, String query) {
		
		//Load Vector Database
		//
		SearchRequest searchRequest = SearchRequest.builder().topK(5).similarityThreshold(0.6).query(query).build();
		List<Document> documents = this.vectorStore.similaritySearch(searchRequest);
		List<String> documentList =documents.stream().map(x->x.getText()).toList();
		String context = String.join(", ", documentList);
		
		logger.info("Conext is {}:", context);
		return this.chatClient
				.prompt()
				.advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, userId))
				.system(sys->sys.text(this.sysetmMessage).param("documents", context))
				.user(us->us.text(this.userMessage).param("query", query))
				.call()
				.content();
	}

	@Override
	public String questionAnswerAdvsiorPrompt(String userId, String query) {
//		return this.chatClient
//				.prompt()
////				.advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
//				.user(query)
//		        .call()
//		        .conent();
		
//		Jr hi ".advisors(QuestionAnswerAdvisor.builder(vectorStore).build())"
//		jr chali asti tr khalche lihaichi garaj nasti .
		SearchRequest searchRequest = SearchRequest.builder()
		        .query(query)
		        .topK(5)
		        .similarityThreshold(0.6)
		        .build();

		List<Document> documents = vectorStore.similaritySearch(searchRequest);

		String context = documents.stream()
		        .map(Document::getText)
		        .collect(Collectors.joining("\n"));

		return chatClient
		        .prompt()
		        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId))
		        .system(system -> system.text("""
		            Answer only using the following context.

		            Context:
		            {documents}

		            If the answer is not present, say "I don't know."
		            """)
		            .param("documents", context))
		        .user(query)
		        .call()
		        .content();
	}

}
