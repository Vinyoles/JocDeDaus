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

	@Field(name="anonymous")
	private boolean anonymous;
	
	
	
	public void assignId(int id) {
		this.id = id;
	}
	
	public void assignLocalDate() {
		registerDate = LocalDate.now();
	}
	
	
	//calculates the mean value of all the games that has made a player
	public double meanValue(List<Game> games){
		int numberOfGames = 0;
		double addedResults = 0;
		
		//searches all the games from a player
		for (Game game : games) {
			if(game.getIdPlayer() == id) {
				addedResults += game.getTotalResult();
				numberOfGames++;
			}
		}
		
		//calculates the player success rate. If the player hasn't played any game, returns 0
		double userMeanValue;
		if (numberOfGames > 0) {
			userMeanValue = addedResults / numberOfGames;
		}
		else {
			userMeanValue = 0;
		}
		return userMeanValue;
	}
	
	//calculates the player win percentage
	public double winPercentage(List<Game> games) {
		int success = 0;
		int fail = 0;
		
 		for (Game game: games) {
			if (game.getIdPlayer() == id) {
				if (game.isWin()) success++;
				else fail++;
			}
		}
		
		//calculates the result from the data on success and failure
		double result;
		if (fail == 0 && success != 0) result = 100;
		else if (fail == 0 && success == 0) result = 0;
		else result = (double) success/(success + fail) * 100;
		
		return result;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		if (anonymous == true) return "anonymous";
		else return userName;
	}
	public void setUserName(String name) {
		this.userName = name;
	}
	public LocalDate getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(LocalDate registerDate) {
		this.registerDate = registerDate;
	}
	public boolean isAnonymous() {
		return anonymous;
	}
	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}
	

	@Override
	public String toString() {
		return "\"ID" + id + "\": {\"userName\": \"" + getUserName() + "\", \"registerDate\": \"" + registerDate + "\", \"gamesPlayed\":{";
	}
}
