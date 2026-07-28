package spring.audio.media.service;

import org.springframework.ai.audio.transcription.AudioTranscriptionOptions;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AudioService {
	
	private final TranscriptionModel transcriptionModel;
	
	public AudioService(TranscriptionModel transcriptionModel) {
		this.transcriptionModel=transcriptionModel;
	}
	
	public ResponseEntity<String> convertAudioToText(Resource resource) {
		return ResponseEntity.ok(transcriptionModel.transcribe(resource));
	}

	public String convertAudioToTextWithOptions(Resource resource) {
		
		return transcriptionModel.transcribe(resource, OpenAiAudioTranscriptionOptions.builder()
				.model("whisper-large-v3-turbo")
				.language("en")
				.temperature(0.7f)
				.build());
	}
}
