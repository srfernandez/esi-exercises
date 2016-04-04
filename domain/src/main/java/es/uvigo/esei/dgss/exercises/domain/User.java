package es.uvigo.esei.dgss.exercises.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	private String login;
	private String name;
	private String password;
	private byte[] picture;

	public User() {
	}

	public User(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
}
