package com.toolcalling.tool_calling.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SimpleDateTimeTool {

    private static final Logger logger =
            LoggerFactory.getLogger(SimpleDateTimeTool.class);

    //Information Tool
    @Tool(description = "Returns the current date and time.")
    public static String getCurrentDateTime() {

        String currentDateTime = LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();

        logger.info("Tool Called");
        logger.info("Returning: {}", currentDateTime);

        return currentDateTime;
    }
    
    //Action Tool
    @Tool(description = "Set the alarm for given time")
    void setAlarm(@ToolParam(description = "Time in ISO-8601 format") String time) {
    	var date = LocalDateTime.parse(time,DateTimeFormatter.ISO_DATE_TIME);
    	this.logger.info("set the alarm for give time {}",date);
    }
}
