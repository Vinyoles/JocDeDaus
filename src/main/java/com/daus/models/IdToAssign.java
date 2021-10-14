package com.daus.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="ids")
public class IdToAssign {
	IdToAssign() {
	}
	
	public IdToAssign(int id, int idStored) {
		this.id = id;
		this.idStored = idStored;
	}

	@Id
	private int id;
	//1:player, 2:game
	
	@Field(name="idStored")
	private int idStored;
	

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdStored() {
		return idStored;
	}
	public void setIdStored(int idStored) {
		this.idStored = idStored;
	}
}
