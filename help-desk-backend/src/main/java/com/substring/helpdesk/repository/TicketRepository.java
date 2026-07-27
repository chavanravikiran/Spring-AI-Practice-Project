package com.substring.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.substring.helpdesk.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{
	
	Optional<Ticket> findById(Long ticketId);

	Optional<Ticket> findByEmail(String email);

}
