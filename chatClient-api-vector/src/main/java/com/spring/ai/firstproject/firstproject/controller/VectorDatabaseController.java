package com.spring.ai.firstproject.firstproject.controller;

import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.ai.firstproject.firstproject.service.VectorDatabaseService;

@RestController
@RequestMapping("/")
public class VectorDatabaseController {
	
	@Autowired
	public VectorDatabaseService vectorDatabaseService;

    @GetMapping("/vectordbChat")
    public ResponseEntity<String> vectordbChat(
    		@RequestHeader("userId") String userId,
            @RequestParam String query) {

        return ResponseEntity.ok(
        		vectorDatabaseService.chatTemplate(userId, query));
    }

    //No need to write manual prompts Spring AI do automatic |QuestionAnswerAdvisor in detail Spring AI
    @GetMapping("/questionAnswerAdvsiorPrompt")
    public ResponseEntity<String> questionAnswerAdvsiorPrompt(
    		@RequestHeader("userId") String userId,
            @RequestParam String query) {

        return ResponseEntity.ok(vectorDatabaseService.questionAnswerAdvsiorPrompt(userId, query));
    }
    
    @GetMapping("/retrievalAugmentationAdvisor")
    public ResponseEntity<String> retrievalAugmentationAdvisor(
    		@RequestHeader("userId") String userId,
            @RequestParam String query) {
    	
        return ResponseEntity.ok(vectorDatabaseService.retrievalAugmentationAdvisor(userId, query));
    }
    
    @GetMapping("/getResponseFromAdvRAG")
    public ResponseEntity<String> getResponseFromAdvRAG(@RequestParam("query") String query,
    		@RequestHeader("userId") String userId){
    	return ResponseEntity.ok(vectorDatabaseService.getResponseFromAdvRAG(query,userId));
    }
}
