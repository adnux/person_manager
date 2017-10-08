package com.andre.example.domain.projection;

import java.time.LocalDate;

import org.springframework.data.rest.core.config.Projection;

import com.andre.example.domain.Person;

@Projection(name = "PersonProjection", types = Person.class)
public interface PersonProjection {

	String getName();

	String getDocument();

	String getEmail();

	LocalDate getBirth();
}
