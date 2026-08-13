package com.ecom.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {

	@Bean
	static ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
