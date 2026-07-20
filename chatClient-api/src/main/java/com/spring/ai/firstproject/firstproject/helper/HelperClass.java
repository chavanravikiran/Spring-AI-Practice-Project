package com.spring.ai.firstproject.firstproject.helper;

import java.util.List;

public class HelperClass {

	public static List<String> getData(){
		return List.of(
				"Helper Class is used to provide reusable utility methods across the application.",
				"Utility Class contains common static methods to reduce code duplication.",
				"Constants Class stores application-wide constant values in a centralized location.",
				"Configuration Class is used to define and manage Spring beans and application settings.",
				"Properties Class is used to bind configuration values from application properties or application_yml.",
				"REST API allows communication between client and server using HTTP methods.",
				"Spring Boot simplifies Java application development with minimal configuration.",
				"Dependency Injection helps create loosely coupled and easily testable applications.",
				"Spring Data JPA simplifies database operations using repository interfaces.",
				"Hibernate is an ORM framework used to map Java objects to database tables.",
				"Spring Security provides authentication and authorization mechanisms for applications.",
				"JWT Authentication enables secure, stateless user authentication.",
				"Global Exception Handling provides a centralized way to handle application exceptions.",
				"Validation ensures that incoming request data meets predefined business rules.",
				"DTO is used to transfer data between different application layers.",
				"ModelMapper simplifies object-to-object mapping between entities and DTOs.",
				"Lombok reduces boilerplate code by generating getters, setters, and constructors.",
				"Docker packages applications and their dependencies into portable containers.",
				"Redis is an in-memory database used for caching and improving application performance.",
				"Kafka enables reliable, scalable, and asynchronous message processing between services."
				);
	}
}
