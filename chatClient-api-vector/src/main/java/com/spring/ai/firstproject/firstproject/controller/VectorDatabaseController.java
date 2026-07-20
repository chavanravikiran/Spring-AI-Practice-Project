package com.spring.ai.firstproject.firstproject.controller;

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

}
