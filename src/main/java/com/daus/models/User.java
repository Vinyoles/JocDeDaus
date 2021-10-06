package com.daus.models;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="users")
public class User {
	
	public User() {	
	}
	
	
	@Id
	private int id;
	
	@Field(name="userName")
	private String userName;
	
	@Field(name="registerDate")
	private LocalDate registerDate;

	
	
	
	public void assignId(int id) {
		this.id = id;
	}
	
	public void assignLocalDate() {
		registerDate = LocalDate.now();
	}
	
	
	
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public String getName() {
		return userName;
	}
	public void setName(String name) {
		this.userName = name;
	}
	public LocalDate getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(LocalDate registerDate) {
		this.registerDate = registerDate;
	}
	
	
	@Override
	public String toString() {
		return "\"ID" + id + "\": {\"userName\": \"" + userName + "\", \"registerDate\": \"" + registerDate + "\", \"gamesPlayed\":{";
	}
}
