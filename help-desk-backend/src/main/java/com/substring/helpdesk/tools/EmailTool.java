package com.substring.helpdesk.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class EmailTool {

	//Sent Email to Support Team
	@Tool(description = "This tool helps to send email to support team regarding new ticket")
	public void sendEmailToSupportTeam(@ToolParam(description = "Email id associated with ticket for contact information")  String email, 
			@ToolParam(description = "Short description of ticket summary") String message) {
		System.out.println("Going to send email to support team !!!!");
		System.out.println("Email Id :-"+email);
		System.out.println("Message  :-"+message);
	}
}
