package docker.model.dockerModelExample;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DockerModelExampleApplicationTests {

	@Autowired
	private ChatClient chatClient;
	
	@Test
	void contextLoads() {
	}

	@Test
	public void testChatClient() {
		System.out.println("Start ChatClient Testing !!!!");
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Please Enter Query ?");
		
		String query = sc.next();
		var response = this.chatClient.prompt(query).call().content();
		System.out.println("response ---->"+response);
		
		
	}
}
