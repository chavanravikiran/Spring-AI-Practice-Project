package com.toolcalling.tool_calling.tools;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherTool {

	@Value("${spring.weather.api-key}")
	private String weatherApiKey;
	
	@Autowired
	private RestClient restClient;
	
	private Logger logger = LoggerFactory.getLogger(WeatherTool.class);
	
	@Tool(description="get weather information of the give city")
	public String getWeather(@ToolParam(description="city of which we want weather information") String city) {
		//call external apis
		logger.info("City name is : {}",city);
	 var resposne =	restClient
		.get()
		.uri(
			builder->builder.path("/current.json")	
			.queryParam("key", weatherApiKey)
			.queryParam("q", city)
			.build()
			)
		.retrieve()
		.body(new ParameterizedTypeReference<Map<String, Object>>() {
			
		});
	 logger.info("Weather Response for city : {},{}",city,resposne.toString());

	 return resposne.toString();
	}
	
}
