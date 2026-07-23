package com.spring.ai.firstproject.firstproject.service;

import java.util.List;

import org.springframework.ai.document.Document;

public interface DataLoaderService {

	List<Document> loadDocumentFromJson();
	
	List<Document> loadDocumentFromPdf();
}
