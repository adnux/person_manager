package com.andre.example.api.rest;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.andre.example.Application;
import com.andre.example.dao.jpa.PersonRepository;
import com.andre.example.domain.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class PeopleControllerTest {

	@InjectMocks
	PeopleController controller;

	@Autowired
	WebApplicationContext context;

	@Autowired
	PersonRepository repository;

	private MockMvc mvc;

	@Before
	public void initTests() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void greaterThanZero() throws Exception {
		mvc.perform(get("/api/people").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()", greaterThan(0)));
	}

	@Test
	public void findByDocument() throws Exception {
		mvc.perform(get("/api/people?document=05169407912").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content.length()", is(1)))
				.andExpect(jsonPath("content[*].document", hasItem("05169407912")))
				.andExpect(jsonPath("content[*].name", hasItem("André Luís Ferreira")))
				.andExpect(jsonPath("content[*].email", hasItem("adnux182@gmail.com")))
				.andExpect(jsonPath("content[*].birth", hasItem("1986-08-06")));
	}

	@Test
	public void findOneById() throws Exception {
		mvc.perform(get("/api/people/59db44c59756ed0cb2103bfd").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.document", is("08782529329")))
				.andExpect(jsonPath("$.name", is("Antonio Carlos")))
				.andExpect(jsonPath("$.email", is("antonio.carlos@gmail.com")))
				.andExpect(jsonPath("$.birth", is("1817-10-10")));
	}

	@Test
	public void shouldCreateAndUpdateAndDelete() throws Exception {
		Person p1 = new Person(null, "Pessoa Teste", "82542284342", "email@email.com", LocalDate.of(2017, 10, 9));
		String p1Json = toJson(p1);

		// CREATE
		MvcResult result = mvc.perform(post("/api/people").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(p1Json)).andExpect(status().isCreated()).andReturn();

		// UPDATE
		String id = result.getResponse().getHeader("id");
		p1.setName("Aristides Demétrio");
		String p1UpJson = toJson(p1);
		result = mvc
				.perform(put("/api/people/" + id).accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON).content(p1UpJson))
				.andExpect(status().isNoContent()).andReturn();

		// RETRIEVE updated
		mvc.perform(get("/api/people/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.document", is("82542284342")))
				.andExpect(jsonPath("$.name", is("Aristides Demétrio")))
				.andExpect(jsonPath("$.email", is("email@email.com"))).andExpect(jsonPath("$.birth", is("2017-10-09")));

		// DELETE
		mvc.perform(delete("/api/people/" + id)).andExpect(status().isNoContent());

		// RETRIEVE should fail
		mvc.perform(get("/api/people/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// REAL DELETE
		repository.delete(id);
	}

	@Test
	public void shouldNotDuplicate() throws Exception {
		Person p1 = new Person(null, "Pessoa Teste", "82542284342", "email@email.com", LocalDate.of(2017, 10, 9));
		String p1Json = toJson(p1);

		// CREATE
		MvcResult result = mvc.perform(post("/api/people").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(p1Json)).andExpect(status().isCreated()).andReturn();

		String id = result.getResponse().getHeader("id");

		// TRY TO DUPLICATE
		mvc.perform(post("/api/people").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(p1Json)).andExpect(status().isBadRequest()).andReturn();

		// REAL DELETE
		repository.delete(id);
	}

	@Test
	public void shouldNotAcceptWrongDate() throws Exception {
		String p1Json = "{name: 'Pessoa Teste', document: '82542284342', email: 'email@email.com', birth: '9876-99-99'}";

		// CREATE
		MvcResult result = mvc.perform(post("/api/people").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(p1Json)).andExpect(status().isCreated()).andReturn();

		String id = result.getResponse().getHeader("id");

		// TRY TO DUPLICATE
		mvc.perform(post("/api/people").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(p1Json)).andExpect(status().isBadRequest()).andReturn();

		// REAL DELETE
		repository.delete(id);
	}

	private String toJson(Object obj) throws Exception {
		ObjectMapper map = new ObjectMapper();
		map.registerModule(new JavaTimeModule());
		map.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return map.writeValueAsString(obj);
	}

}
