package com.daus.models;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="users")
public class User {
	
	
	
	
	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY) //TODO
	private int ID;
	
	@Field(name="userName")
	private String userName;
	
	@Field(name="registerDate")
	private Timestamp registerDate;
	
	
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return userName;
	}
	public void setName(String name) {
		this.userName = name;
	}
	public Timestamp getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Timestamp registerDate) {
		this.registerDate = registerDate;
	}
	
	
	@Override
	public String toString() {
		return "User [ID=" + ID + ", userName=" + userName + ", registerDate=" + registerDate + "]";
	}
}
