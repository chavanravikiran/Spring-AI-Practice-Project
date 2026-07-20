package com.spring.ai.firstproject.firstproject.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Tutorial {
	
	private String title;
	private String content;
	private LocalDate createdYear;
}
