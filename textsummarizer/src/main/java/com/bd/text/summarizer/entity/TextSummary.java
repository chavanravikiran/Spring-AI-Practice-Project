package com.bd.text.summarizer.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="text_summaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextSummary {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="original_text",columnDefinition = "TEXT", nullable = false)
	private String originalText;

	@Column(name="summarized_text",columnDefinition = "TEXT")	
	private String summarizedText;
	
	@Column(name="created_at")	
	private LocalDateTime createdAt;

	@Column(name="model_used")	
	private String modelUsed;

	@PrePersist
	protected void onCreated() {
		createdAt = LocalDateTime.now();
	}
}
