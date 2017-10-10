package com.andre.example.dao.jpa.impl;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.andre.example.dao.jpa.PersonRepository;
import com.andre.example.dao.jpa.PersonRepositoryCustom;
import com.andre.example.domain.Person;

public class PersonRepositoryImpl implements PersonRepositoryCustom {

	@Autowired
	private MongoOperations mongoOps;

	@Autowired
	private PersonRepository repository;

	@Override
	public Page<Person> findByNameOrDocumentOrEmailOrBirth(Person filter, Pageable pageable) {

		Query query = new Query();
		if (isNotEmpty(filter.getName())) {
			query.addCriteria(Criteria.where("name").regex(filter.getName(), "i"));
		}
		if (isNotEmpty(filter.getDocument())) {
			query.addCriteria(Criteria.where("document").is(filter.getDocument()));
		}
		if (isNotEmpty(filter.getEmail())) {
			query.addCriteria(Criteria.where("email").is(filter.getEmail()));
		}
		if (nonNull(filter.getBirth())) {
			query.addCriteria(Criteria.where("birth").is(filter.getBirth()));
		}
		query.addCriteria(Criteria.where("deleted").ne(true));

		List<Person> list = mongoOps.find(query.with(pageable), Person.class);
		return PageableExecutionUtils.getPage(list, pageable, () -> mongoOps.count(query, Person.class));
	}

}
