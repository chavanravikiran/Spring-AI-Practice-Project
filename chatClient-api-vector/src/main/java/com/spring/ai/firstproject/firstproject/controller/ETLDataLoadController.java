package com.spring.ai.firstproject.firstproject.controller;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.ai.firstproject.firstproject.service.DataLoaderService;
import com.spring.ai.firstproject.firstproject.service.DocumentTrasnformerService;

@RestController
@RequestMapping("/")
public class ETLDataLoadController {

	@Autowired
	public DataLoaderService dataLoaderService;
	
	@Autowired
	private VectorStore vectorStore;

//	public VectorDatabaseServiceImpl(ChatClient chatClient,VectorStore vectorStore) {
//		this.chatClient = chatClient;
//		this.vectorStore = vectorStore;
//	}
	
	@Autowired
	public DocumentTrasnformerService documentTrasnformerService;
	
	@GetMapping("/jsonDataLoad")
	public List<Document> jsonDataLoad(){
		return dataLoaderService.loadDocumentFromJson();
	}
	
	@GetMapping("/pdfDataLoad")
	public List<Document> pdfDataLoad(){
		List<Document> result = dataLoaderService.loadDocumentFromPdf();
		result.forEach(item->{
			System.out.println(item);
			System.out.println("----------");
		});
		
		List<Document> transformDocuments = documentTrasnformerService.transform(result);
		System.out.println("Below is Transform Documents: "+transformDocuments);
		
		vectorStore.add(transformDocuments);
		System.out.println("Save trasnform Document Data in Vector Database :");
		return dataLoaderService.loadDocumentFromPdf();
	}
}
