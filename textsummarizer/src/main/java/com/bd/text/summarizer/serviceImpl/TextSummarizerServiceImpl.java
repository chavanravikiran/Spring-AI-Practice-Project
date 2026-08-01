package com.bd.text.summarizer.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bd.text.summarizer.dto.TextSummaryRepository;
import com.bd.text.summarizer.entity.SummarizeRequest;
import com.bd.text.summarizer.entity.SummarizeResponse;
import com.bd.text.summarizer.entity.TextSummary;
import com.bd.text.summarizer.service.TextSummarizerService;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Service
public class TextSummarizerServiceImpl implements TextSummarizerService{

	private Logger logger = org.slf4j.LoggerFactory.getLogger(TextSummarizerServiceImpl.class);
	
	@Autowired
	private TextSummaryRepository textSummaryRepository;
	
	@Value("${spring.ai.google.genai.api-key}")
	private String geminiApiKey;

	@Value("${spring.ai.google.genai.chat.options.model}")
	private String geminiModel;
	
	@Override
	public SummarizeResponse summarizeText(SummarizeRequest request) {
		validateRequest(request);
		
		String inputText = request.getText();
		String prompt = buildPromptForSummarization(inputText);
		Client client = createGeminiClient();
		String summarizedText = generateSummaryWithErrorHandling(client, prompt);
		
		TextSummary savedSummary = saveSummaryToDatabase(inputText,summarizedText); 
		
		return buildSummarizeResponse(savedSummary);
	}

	private void validateRequest(SummarizeRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("Request can not be null");
		}
		if(request.getText() == null || request.getText().trim().isEmpty()) {
			throw new IllegalArgumentException("Text can not be null or empty");
		}
	}

	private String buildPromptForSummarization(String inputText) {
		return "Please provide a concise summary point wise of the following text:\n\n" +inputText;
	}

	private Client createGeminiClient() {
		return new Client.Builder()
				.apiKey(geminiApiKey)
				.build();
	}
	
	private String generateSummaryWithErrorHandling(Client client, String prompt) {
		try {
			GenerateContentResponse response = client.models.generateContent(geminiModel, prompt, null);
			return extractTextFromResponse(response);
		}catch (Exception e) {
			logger.error("Error calling Gemini API: {} - {}", e.getMessage(), e);
			throw new RuntimeException("Error Calling Gemini API {}",e);
		}
	}


	private String extractTextFromResponse(GenerateContentResponse response) {
		if(response != null && response.text() != null && !response.text().isEmpty()) {
			return response.text();
		}
		throw new RuntimeException("Unable to extract summary from Gemini Response");
	}

	private TextSummary saveSummaryToDatabase(String inputText, String summarizedText) {
		TextSummary textSummary = new TextSummary();
		textSummary.setOriginalText(inputText);
		textSummary.setSummarizedText(summarizedText);
		textSummary.setModelUsed(geminiModel);
		
		TextSummary savedSummary = textSummaryRepository.save(textSummary);
		
		logger.info("Saved summary to databasewith ID: {}" , savedSummary.getId());
		return savedSummary;
	}

	private SummarizeResponse buildSummarizeResponse(TextSummary savedSummary) {
		
		return new SummarizeResponse(
				savedSummary.getId(),
				savedSummary.getOriginalText(),
				savedSummary.getSummarizedText(),
				savedSummary.getModelUsed()
				);
	}


	@Override
	public List<TextSummary> getAllSummaries() {
		return textSummaryRepository.findAllByOrderByCreatedAtDesc();
	}

	@Override
	public TextSummary getSummaryById(Long id) {
		return textSummaryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Summary Not Found by given id "+id));
	}

	@Override
	public void deleteSummary(Long id) {
		TextSummary summary = getSummaryById(id);
		textSummaryRepository.deleteById(id);
		logger.info("Delete Summary with ID {} "+id);
		
	}
}
