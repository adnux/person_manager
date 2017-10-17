package com.andre.example.dao.jpa;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.andre.example.Application;
import com.andre.example.domain.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    @Test
    public void findByDocumentTest() {
        Person person = repository.findByDocument("05169407912");
        assertThat(person, is(notNullValue()));
        assertThat(person.getDocument(), is("05169407912"));
    }

    @Test
    public void findByIdAndDocumentNotTest() {
        Person person = repository.findByIdNotAndDocument("", "05169407912");
        assertThat(person, is(notNullValue()));
        assertThat(person.getDocument(), is("05169407912"));
    }

    @Test
    public void dontFindByIdAndDocumentNotTest() {
        Person person = repository.findByIdNotAndDocument("59d5c2c39756ed75253c495b", "05169407912");
        assertThat(person, is(nullValue()));
    }

    @Test
    public void findByNameOrDocumentOrEmailOrBirth() {
        Person filter = new Person(
                null, "André Luís Ferreira", "05169407912", "adnux182@gmail.com", LocalDate.of(1986, 8, 6));
        Pageable pageable = new PageRequest(0, 10);
        Page<Person> page = repository.findByNameOrDocumentOrEmailOrBirth(filter, pageable);
        assertThat(page, is(notNullValue()));
        assertThat(page.getContent(), is(not(empty())));
        assertThat(page.getContent().size(), is(1));
    }
}
