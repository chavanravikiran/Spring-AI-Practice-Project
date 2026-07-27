package com.substring.helpdesk.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.substring.helpdesk.entity.Ticket;
import com.substring.helpdesk.repository.TicketRepository;
import com.substring.helpdesk.services.TicketService;

import jakarta.transaction.Transactional;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepository;
	
	
	// create Ticket
//	@Transactional
//	public Ticket createTicket(Ticket ticket) {
//		ticket.setId(null);
//		return ticketRepository.save(ticket);
//	}
	@Transactional
	public Ticket createTicket(Ticket ticket) {
	    System.out.println("Before null = " + ticket.getId());
	    ticket.setId(null);
	    System.out.println("After null = " + ticket.getId());
	    return ticketRepository.save(ticket);
	}
	
	// update Ticket
	public Ticket updateTicket(Ticket ticket) {
		Ticket ticketObj = ticketRepository.findById(ticket.getId()).orElse(null);
		if(ticketObj !=null) {
//			ticketObj.setStatus(Status.OPEN);
			return ticketRepository.save(ticket);
		}
		return null;
	}	

	// get Ticket logic
	public Ticket getTicket(Long ticketId) {
		return ticketRepository.findById(ticketId).orElse(null);
	}

	// get Ticket by username
	public Ticket getTicketByEmailId(String email) {
		return ticketRepository.findByEmail(email).orElse(null);
	}
	
	
}

