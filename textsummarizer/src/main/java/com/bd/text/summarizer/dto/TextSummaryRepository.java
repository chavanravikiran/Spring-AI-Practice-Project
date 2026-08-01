package com.bd.text.summarizer.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bd.text.summarizer.entity.TextSummary;

@Repository
public interface TextSummaryRepository extends JpaRepository<TextSummary, Long>{

	List<TextSummary> findAllByOrderByCreatedAtDesc();
	
}
