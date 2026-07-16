package com.spring.ai.firstproject.firstproject.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.spring.ai.firstproject.firstproject.entity.Tutorial;

import reactor.core.publisher.Flux;

public interface ChatService {

	String chat(String query);

	Tutorial entityResponse(String query);

	List<Tutorial> listOfEntityResponse(String query);

	String promptTemplate(String query);

	String dynamicQueryPrompt(String query);

	String fluentPrompt(String query);

	String simpleLoggerAdvisor(String query);

	String safeGardAdvisor(String query);

	Flux<String> streamingConcept(String query);

}

