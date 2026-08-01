package com.bd.text.summarizer.service;

import java.util.List;

import com.bd.text.summarizer.entity.SummarizeRequest;
import com.bd.text.summarizer.entity.SummarizeResponse;
import com.bd.text.summarizer.entity.TextSummary;

public interface TextSummarizerService {

	SummarizeResponse summarizeText(SummarizeRequest request);

	List<TextSummary> getAllSummaries();

	TextSummary getSummaryById(Long id);

	void deleteSummary(Long id);

}
