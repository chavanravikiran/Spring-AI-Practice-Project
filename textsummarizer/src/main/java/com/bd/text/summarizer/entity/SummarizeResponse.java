package com.bd.text.summarizer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummarizeResponse {

	private Long id;
	private String originalText;
	private String summarizedText;
	private String modelUsed;
	
}
