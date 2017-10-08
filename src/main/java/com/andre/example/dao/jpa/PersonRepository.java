package com.andre.example.dao.jpa;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.andre.example.domain.Person;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends MongoRepository<Person, String>, PersonRepositoryCustom {

	@RestResource(exported = false)
	Person findByDocument(@Param("document") String document);

	@RestResource(exported = false)
	Person findByIdAndDocumentNot(@Param("id") String id, @Param("document") String document);

}
