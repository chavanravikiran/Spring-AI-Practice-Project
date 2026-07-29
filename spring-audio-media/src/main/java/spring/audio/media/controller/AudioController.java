package spring.audio.media.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import spring.audio.media.service.AudioService;

@RestController
@RequestMapping("/api/v1/audio")
public class AudioController {

	//voice-sample-96kbps.m4a
//	@Value("classpath:/voice-sample-96kbps.m4a")//English Audio
	@Value("classpath:/pure-tone.wav")//English Audio
//	@Value("classpath:/rahulsapkal-golden-era-bollywood-love-song-557305.mp3")//hindi
	private Resource resource;
	
	private final AudioService audioService;
	
	public AudioController(AudioService audioService) {
		this.audioService = audioService;
	}
	
	@PostMapping("/transcription")
	public ResponseEntity<String>generateTextFromAudio(){
		return audioService.convertAudioToText(resource);
	}

	@PostMapping("/transcription-with-options")
	public String generateTextFromAudioWithOptions(){
		return audioService.convertAudioToTextWithOptions(resource);
	}
	
	@PostMapping("/speechToText")
	public String speechToText(@RequestParam("audioFile") MultipartFile multipartFile){
		return audioService.convertAudioToTextWithOptions(multipartFile.getResource());
	}
}
