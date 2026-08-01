package com.bd.text.summarizer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bd.text.summarizer.entity.SummarizeRequest;
import com.bd.text.summarizer.entity.SummarizeResponse;
import com.bd.text.summarizer.entity.TextSummary;
import com.bd.text.summarizer.service.TextSummarizerService;

@RestController
@RequestMapping("/api/summarizer")
@CrossOrigin(origins = "*")
public class TextSummarizerController {

	@Autowired
	private TextSummarizerService textSummarizerService;
	
	@PostMapping("summarize")
	public ResponseEntity<?>summarizeText(@RequestBody SummarizeRequest request){
		try {
			SummarizeResponse response = textSummarizerService.summarizeText(request);
			return ResponseEntity.ok(response);
		}catch (IllegalArgumentException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			errorResponse.put("status", "error");
			return ResponseEntity.badRequest().body(errorResponse);
		}
	}
	
	@GetMapping("summaries")
	public ResponseEntity<List<TextSummary>> getAllSummaries(){
		List<TextSummary> summaries = textSummarizerService.getAllSummaries();
		return ResponseEntity.ok(summaries);
	}
	
	@GetMapping("summaries/{id}")
	public ResponseEntity<?>getSummaryById(@PathVariable Long id){
		try {
			TextSummary summary = textSummarizerService.getSummaryById(id);
			return ResponseEntity.ok(summary);
		}catch (IllegalArgumentException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			errorResponse.put("status", "error");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	@DeleteMapping("summaries/{id}")
	public ResponseEntity<Map<String,String>>deleteSummary(@PathVariable Long id){
		try {
			textSummarizerService.deleteSummary(id);
			Map<String, String> response = new HashMap<>();
			response.put("message", "Summary with id "+id+" has been deleted.");
			response.put("status", "success");
			return ResponseEntity.ok(response);
		}catch (IllegalArgumentException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", e.getMessage());
			errorResponse.put("status", "error");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	@GetMapping("/health")
	public ResponseEntity<String>healthCheck(){
		return ResponseEntity.ok("Text Summary API is Running !!!");
	}
	
}
