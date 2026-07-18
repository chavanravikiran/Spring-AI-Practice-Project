package com.spring.ai.firstproject.firstproject.service;

import reactor.core.publisher.Flux;

public interface MemoryService {

	Flux<String> stream(String conversationId, String query);

	String memory(String conversationId, String query);

	String memorySession(String userId, String conversationId);

}
