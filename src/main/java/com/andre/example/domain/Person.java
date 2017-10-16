package com.andre.example.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.andre.example.validator.ExtendedEmailValidator;

@Entity
@Table(name = "person")
public class Person {

    @Id
    private String id;

    @NotBlank
    @Size(max = 150)
    private String name;

    @CPF
    private String document;

    @ExtendedEmailValidator
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private boolean deleted;

    public Person() {
    }

    public Person(String id, String name, String document, String email, LocalDate birth) {
        super();
        this.id = id;
        this.name = name;
        this.document = document;
        this.email = email;
        this.birth = birth;
    }

    public String getId() {
        return id;
    }

    public void defineObjectId(String id) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Person [id=");
        builder.append(id);
        if (name != null) {
            builder.append(", ");
            builder.append("name=");
            builder.append(name);
        }
        if (document != null) {
            builder.append(", ");
            builder.append("document=");
            builder.append(document);
        }
        if (email != null) {
            builder.append(", ");
            builder.append("email=");
            builder.append(email);
        }
        if (birth != null) {
            builder.append(", ");
            builder.append("birth=");
            builder.append(birth);
        }
        builder.append("]");
        return builder.toString();
    }

}
