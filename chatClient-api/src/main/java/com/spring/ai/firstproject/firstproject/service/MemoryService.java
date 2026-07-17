package com.spring.ai.firstproject.firstproject.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.spring.ai.firstproject.firstproject.entity.Tutorial;

import reactor.core.publisher.Flux;

public interface MemoryService {

	Flux<String> stream(String conversationId, String query);

	String memory(String conversationId, String query);

}
