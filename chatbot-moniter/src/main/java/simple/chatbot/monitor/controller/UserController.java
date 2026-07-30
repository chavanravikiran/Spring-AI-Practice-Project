package simple.chatbot.monitor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simple.chatbot.monitor.dto.UserDto;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@PostMapping()
	public ResponseEntity<UserDto>getUser(){
		UserDto userDto =new UserDto("2344","Ravikiran","Pune");
		return ResponseEntity.ok(userDto);
	}
	
}
