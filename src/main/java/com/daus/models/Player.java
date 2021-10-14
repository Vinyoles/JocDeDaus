package com.daus.models;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="players")
public class Player {
	
	public Player() {	
	}
	
	
	@Id
	private String uuid;
	
	@Field(name="playerName")
	private String playerName;
	
	@Field(name="registerDate")
	private LocalDate registerDate;

	@Field(name="anonymous")
	private boolean anonymous;
	
	
	//calculates an uuid and assigns it as an integer to the user
	public void assignUUID() {
		this.uuid = UUID.randomUUID().toString();
	}
	
	//assigns the local date
	public void assignLocalDate() {
		registerDate = LocalDate.now();
	}
	
	
	//calculates the mean value of all the games that has made a player
	public double meanValue(List<Game> games){
		int numberOfGames = 0;
		double addedResults = 0;
		
		//searches all the games from a player
		for (Game game : games) {
			if(game.getUuidPlayer().equals(uuid)) {
				addedResults += game.getTotalResult();
				numberOfGames++;
			}
		}
		
		//calculates the player success rate. If the player hasn't played any game, returns 0
		double playerMeanValue;
		if (numberOfGames > 0) {
			playerMeanValue = addedResults / numberOfGames;
		}
		else {
			playerMeanValue = 0;
		}
		return playerMeanValue;
	}
	
	//calculates the player win percentage
	public double winPercentage(List<Game> games) {
		int success = 0;
		int fail = 0;
		
 		for (Game game: games) {
			if (game.getUuidPlayer().equals(uuid)) {
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
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPlayerName() {
		if (anonymous == true) return "anonymous";
		else return playerName;
	}
	public void setPlayerName(String name) {
		this.playerName = name;
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
		return "\"ID" + uuid + "\": {\"playerName\": \"" + getPlayerName() + "\", \"registerDate\": \"" + registerDate + "\", \"gamesPlayed\":{";
	}
}
