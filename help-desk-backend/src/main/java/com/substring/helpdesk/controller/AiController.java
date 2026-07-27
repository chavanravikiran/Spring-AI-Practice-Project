package com.substring.helpdesk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.substring.helpdesk.servicesImpl.AiService;


@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

	private final AiService service;

	public AiController(AiService service) {
		this.service = service;
	}
	
	@GetMapping("/")
	public ResponseEntity<String>getReponse(@RequestBody String query,@RequestHeader("conversationId") String conversationId){
		System.out.println("----------------->"+query);
		return ResponseEntity.ok(service.getReponseFromAssistant(query,conversationId));
	}
	
}
