package com.andre.example.dao.jpa;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.andre.example.domain.Person;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends MongoRepository<Person, ObjectId> {

	Person findByDocument(@Param("documento") String documento);

	List<Person> findByName(@Param("name") String name);

	Page<Person> findAll(Pageable pageable);

}
