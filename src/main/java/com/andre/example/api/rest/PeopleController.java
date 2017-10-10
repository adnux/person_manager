package com.andre.example.api.rest;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.andre.example.dao.jpa.PersonRepository;
import com.andre.example.domain.Person;
import com.andre.example.validator.PersonValidator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/people")
@Api(tags = { "people" })
@CrossOrigin
public class PeopleController extends AbstractRestHandler {

	private final PersonRepository personRepository;

	public PeopleController(PersonRepository personRepository) {
		super();
		this.personRepository = personRepository;
	}

	@InitBinder("person")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new PersonValidator(personRepository));
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get a paginated list of all people.", notes = "The list is paginated. You can provide a page number and a page size")
	@ResponseBody
	public Page<Person> getAllPeople(@ApiParam(value = "Parameters to filter") Person filter,
			@ApiParam(value = "Pagination information") Pageable pageable) {
		return this.personRepository.findByNameOrDocumentOrEmailOrBirth(filter, pageable);
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get a person.")
	@ResponseBody
	public Person getPerson(@PathVariable("id") String id) {
		return this.personRepository.findOne(id);
	}

	@PostMapping
	@ApiOperation(value = "Create a new person.")
	public Person create(@Valid @RequestBody Person person) {
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Validator validator = vf.getValidator();
		validator.validate(person);
		return this.personRepository.save(person);
	}

	@PutMapping(value = "/{id}")
	@ApiOperation(value = "Update person information.")
	public Person update(@PathVariable("id") String id, @RequestBody Person person) {
		checkResourceFound(this.personRepository.findOne(id));
		person.defineObjectId(id);
		return this.personRepository.save(person);
	}

	@DeleteMapping(value = "/{id}")
	@ApiOperation(value = "Delete the person.")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		Person person = this.personRepository.findOne(id);
		checkResourceFound(this.personRepository.findOne(id));
		person.setDeleted(true);
		this.personRepository.save(person);
	}
}
