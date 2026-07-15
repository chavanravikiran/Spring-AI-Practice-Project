package com.spring.ai.firstproject.firstproject.service;

import java.util.List;

import com.spring.ai.firstproject.firstproject.entity.Tutorial;

public interface ChatService {

	String chat(String query);

	Tutorial entityResponse(String query);

	List<Tutorial> listOfEntityResponse(String query);

	String promptTemplate(String query);

	String dynamicQueryPrompt(String query);

	String fluentPrompt(String query);

}

