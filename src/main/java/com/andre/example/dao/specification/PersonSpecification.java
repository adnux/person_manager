package com.andre.example.dao.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.andre.example.domain.Person;

public class PersonSpecification implements Specification<Person> {

	private final Person criteria;

	public PersonSpecification(Person criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

}
