package com.daus.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="ids")
public class IdToAssign {
	IdToAssign() {
	}
	
	public IdToAssign(int id, int idUser) {
		this.id = id;
		this.idUser = idUser;
	}

	@Id
	private int id;
	
	@Field(name="idUser")
	private int idUser;

	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
}
