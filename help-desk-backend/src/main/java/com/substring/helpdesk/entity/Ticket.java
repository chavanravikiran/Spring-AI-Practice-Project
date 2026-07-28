package com.substring.helpdesk.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="help_desk_tickets")
@Data
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	private String summary;
	
	@Enumerated(EnumType.STRING)
	private Priority priority;
	
	@Column(length = 1000)
	private String description;
	
	@Column(length = 1000)
	private String category;
	
	@Column(unique = true)
	private String email;
	
	private LocalDateTime createdOn;
	
	private LocalDateTime updatedOn;
		
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@PrePersist
	void preSave() {
		if(this.createdOn == null) {
			this.createdOn =LocalDateTime.now();
		}else {
			this.updatedOn = LocalDateTime.now();
		}
	}
	
	@PreUpdate
	void preUpdate() {
		this.updatedOn = LocalDateTime.now();
	}
}
