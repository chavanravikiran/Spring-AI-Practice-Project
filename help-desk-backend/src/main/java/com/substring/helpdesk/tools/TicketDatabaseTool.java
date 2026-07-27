package com.substring.helpdesk.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.substring.helpdesk.entity.Ticket;
import com.substring.helpdesk.services.TicketService;

@Component
//@RequiredArgsConstructor
public class TicketDatabaseTool {

	@Autowired
	private TicketService ticketService;
	
	//create Ticket Tool
	@Tool(description = "This tool helps to create new ticket in database.")
	public Ticket createTicketTool(@ToolParam(description = "Ticket fields required to create new ticket") Ticket ticket){
		try {
			System.out.println("Going to create Ticket......");
			System.out.println(ticket);
			return ticketService.createTicket(ticket);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Tool(description = "This tool helps to get Ticket by email.")
	public Ticket getTicketbyUserName(@ToolParam(description = "email id whose ticket is required") String emailid) {
		return ticketService.getTicketByEmailId(emailid);
	}
	
	@Tool(description = "This tool helps to update ticket")
	public Ticket updateticket(@ToolParam(description = "new ticket details with ticket id ") Ticket ticket) {
		return ticketService.updateTicket(ticket);
	}
	
	@Tool(description = "This tool helps to get Current date and time.")
	public String getCurrentDateTime() {
		return String.valueOf(System.currentTimeMillis());
	}
	
}
