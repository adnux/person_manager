package com.andre.example.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.andre.example.validator.ExtendedCpfValidator;
import com.andre.example.validator.ExtendedEmailValidator;

@Entity
@Table(name = "person")
public class Person {

	@Id
	private ObjectId id;

	@NotBlank
	@Size(max = 150)
	private String name;

	@Column(unique = true)
	@ExtendedCpfValidator
	private String document;

	@ExtendedEmailValidator
	private String email;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birth;

	public Person() {
	}

	public Person(ObjectId id, String name, String document, String email, LocalDate birth) {
		super();
		this.id = id;
		this.name = name;
		this.document = document;
		this.email = email;
		this.birth = birth;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Person [id=");
		builder.append(id);
		builder.append(", ");
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (document != null) {
			builder.append("document=");
			builder.append(document);
			builder.append(", ");
		}
		if (email != null) {
			builder.append("email=");
			builder.append(email);
			builder.append(", ");
		}
		if (birth != null) {
			builder.append("birth=");
			builder.append(birth);
		}
		builder.append("]");
		return builder.toString();
	}

}
