package com.daus.models;

import java.time.LocalDate;
import java.util.List;

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
	
	
	//calculates the mean value of all the games that has made a player
	public double meanValue(List<Game> games, int playerID){
		int numberOfGames = 0;
		double addedResults = 0;
		
		//searches all the games from a player
		for (Game game : games) {
			if(game.getIdPlayer() == playerID) {
				addedResults += game.getTotalResult();
				numberOfGames++;
			}
		}
		
		//calculates the player success rate. If the player hasn't played any game, returns 0
		double userPercentage;
		if (numberOfGames > 0) {
			userPercentage = addedResults / numberOfGames;
		}
		else {
			userPercentage = 0;
		}
		return userPercentage;
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
