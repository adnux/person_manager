package com.andre.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bedatadriven.jackson.datatype.jts.JtsModule;

@Configuration
public class JacksonConfiguration {

	@Bean
	public JtsModule jtsModule() {
		return new JtsModule();
	}

}
