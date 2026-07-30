package spring.audio.media.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import java.util.logging.Logger;
//
//import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ImageService {

	//This is For OpenAi
//	private final ImageModel imageModel;
//
//	private final Logger logger = LoggerFactory.getLogger(ImageService.class);
//
//	public ImageService(ImageModel imageModel) {
//		this.imageModel = imageModel;
//	}
//	
//	public String generateImage(String prompt) {
//	 
//		OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
//		.n(1)
//		.quality("high")
//		.build();
//	
//		logger.info("Image options {} ",imageOptions);
//		
//		ImageResponse imageResponse = imageModel.call(new ImagePrompt(
//				prompt,imageOptions
//				));
//		
//		logger.info("Image Response {} ",imageResponse);
//		return null;
//	}
	
	private final RestClient restClient;

    @Value("${huggingface.api.token}")
    private String token;

    public ImageService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String generateImage(String prompt) throws IOException {

        byte[] image = restClient.post()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body("""
                      {
                         "inputs":"%s"
                      }
                      """.formatted(prompt))
                .retrieve()
                .body(byte[].class);

        Path output = Path.of("generated-image.png");

        Files.write(output, image);

        return output.toAbsolutePath().toString();
    }
}
