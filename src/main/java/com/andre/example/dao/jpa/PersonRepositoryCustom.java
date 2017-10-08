package com.andre.example.dao.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.andre.example.domain.Person;

public interface PersonRepositoryCustom {

	Page<Person> findByNameOrDocumentOrEmailOrBirth(Person filter, Pageable pageable);

}
