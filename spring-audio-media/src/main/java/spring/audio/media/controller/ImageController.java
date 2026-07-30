package spring.audio.media.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.audio.media.service.ImageService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

	private final ImageService imageService;
	
	public ImageController(ImageService imageService) {
		this.imageService = imageService;
	}

	@PostMapping("/generate")
	public String generateImage(@RequestParam("prompt") String prompt) throws IOException {
		return imageService.generateImage(prompt);
	}

}
