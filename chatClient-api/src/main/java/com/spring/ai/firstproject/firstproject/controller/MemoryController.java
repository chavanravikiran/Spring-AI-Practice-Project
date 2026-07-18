package com.spring.ai.firstproject.firstproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.ai.firstproject.firstproject.service.MemoryService;

import reactor.core.publisher.Flux;
@RestController
@RequestMapping("/")
public class MemoryController {
	
	@Autowired
	public MemoryService memoryService;

    @GetMapping("/memory")
    public ResponseEntity<String> chat(
            @RequestParam(required = false) String conversationId,
            @RequestParam(required = false) String query) {

        System.out.println("conversationId = " + conversationId);
        System.out.println("query = " + query);

        return ResponseEntity.ok(
        		memoryService.memory(conversationId, query));
    }

    @GetMapping(value = "/stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(
            @RequestParam String conversationId,
            @RequestParam String query) {

        return memoryService.stream(conversationId, query);
    }
    
    @GetMapping("/memory-session")
    public ResponseEntity<String> memorySession(
            @RequestHeader String userId,
            @RequestParam String query) {

        return ResponseEntity.ok(
                memoryService.memorySession(userId, query));
    }
	
}
