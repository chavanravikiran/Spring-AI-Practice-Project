package docker.model.dockerModelExample.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

	@Bean
	public ChatClient chatClient(ChatClient.Builder builder) {
		return chatClient(builder);
	}
}
