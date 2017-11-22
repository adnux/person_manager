package com.andre.example.validator;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.andre.example.dao.jpa.PersonRepository;
import com.andre.example.domain.Person;

public class PersonValidator implements Validator {

	@Autowired
	private final PersonRepository repository;

	public PersonValidator(PersonRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Person person = (Person) target;

		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		javax.validation.Validator validator = vf.getValidator();
		Set<ConstraintViolation<Person>> validate = validator.validate(person);

		if (!isCPFValid(person)) {
			errors.rejectValue("document", "document.invalid", "CPF Já cadastrado");
		}

	}

	private boolean isCPFValid(Person person) {
		if (isEmpty(person.getId())) {
			Person exsiting = repository.findByDocument(person.getDocument());
			return isNull(exsiting);
		} else {
			Person exsiting = repository.findByIdNotAndDocument(person.getId(), person.getDocument());
			return isNull(exsiting);
		}
	}
}
