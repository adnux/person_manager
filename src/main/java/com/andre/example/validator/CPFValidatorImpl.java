package com.andre.example.validator;

import static java.util.Objects.nonNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.andre.example.dao.jpa.PersonRepository;
import com.andre.example.domain.Person;

public class CPFValidatorImpl implements ConstraintValidator<ExtendedCpfValidator, String> {

	private final PersonRepository repository;

	public CPFValidatorImpl(PersonRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public void initialize(ExtendedCpfValidator constraintAnnotation) {
	}

	@Override
	public boolean isValid(String documento, ConstraintValidatorContext context) {
		Person person = repository.findByDocument(documento);
		if (nonNull(person)) {
			return false;
		} else {
			return true;
		}
	}

}
