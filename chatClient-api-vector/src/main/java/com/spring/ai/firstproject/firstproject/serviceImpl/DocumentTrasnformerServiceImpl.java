package com.spring.ai.firstproject.firstproject.serviceImpl;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import com.spring.ai.firstproject.firstproject.service.DocumentTrasnformerService;

@Service
public class DocumentTrasnformerServiceImpl implements DocumentTrasnformerService{

	@Override
	public List<Document> transform(List<Document> document) {
		var splitter = new TokenTextSplitter();
		List<Document> transform = splitter.transform(document);
		return transform;
	}

}
