package com.andre.example.test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.andre.example.Application;
import com.andre.example.domain.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class PersonValidationTest {

    private Validator validator;

    @Before
    public void init() {

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();

    }

    @Test
    public void validPersonTest() {
        Person validPerson = new Person(null, "valido", "05169407912", "email@email.com", LocalDate.of(1986, 8, 6));
        Set<ConstraintViolation<Person>> violations = this.validator.validate(validPerson);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void invalidPersonTest() {
        Person validPerson = new Person(null, "", "05169407912999", "email@email", null);
        Set<ConstraintViolation<Person>> violations = this.validator.validate(validPerson);
        violations.stream().forEach(v -> {
            System.out.println("violations:" + v.getMessage() + ": " + v.getPropertyPath().toString());
        });
        assertTrue(!violations.isEmpty());
    }

}
