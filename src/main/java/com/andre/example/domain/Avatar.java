package com.andre.example.domain;

//
// import java.time.LocalDate;
//
// import javax.persistence.Entity;
// import javax.persistence.Id;
// import javax.persistence.Table;
//
// import org.springframework.data.mongodb.core.mapping.DBRef;
//
// @Entity
// @Table(name = "avatar")
// public class Avatar {
//
// @Id
// private String id;
//
// @DBRef(lazy = true)
// private Person person;
//
// private byte[] img;
//
// public Avatar() {
// }
//
// public Avatar(String id, Person person, byte[] img, String email, LocalDate
// birth) {
// super();
// this.id = id;
// this.person = person;
// this.img = img;
// }
//
// public String getId() {
// return id;
// }
//
// public Person getPerson() {
// return person;
// }
//
// public void setPerson(Person person) {
// this.person = person;
// }
//
// public byte[] getImg() {
// return img;
// }
//
// public void setImg(byte[] img) {
// this.img = img;
// }
//
// @Override
// public String toString() {
// StringBuilder builder = new StringBuilder();
// builder.append("Person [id=");
// builder.append(id);
// if (person != null) {
// builder.append(", ");
// builder.append("person=");
// builder.append(person);
// }
// builder.append("]");
// return builder.toString();
// }
//
// }
