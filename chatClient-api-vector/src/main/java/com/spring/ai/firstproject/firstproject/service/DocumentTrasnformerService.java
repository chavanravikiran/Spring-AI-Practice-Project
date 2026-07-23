package com.spring.ai.firstproject.firstproject.service;

import java.util.List;

import org.springframework.ai.document.Document;

public interface DocumentTrasnformerService {
	
	List<Document> transform(List<Document> document);

}
