package com.daus.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="games")
public class Game {
	 
	@Id
	private int idGame;
	
	@Field(name="die1")
	private int die1Result;
	
	@Field(name="die2")
	private int die2Result;
	
	@Field(name="totalResult")
	private int totalResult;
	
	@Field(name="win")
	private boolean win;
	
	@Field(name="uuidPlayer")
	private String uuidPlayer;

	
	
	//returns the value of a dice throw
	public void playGame(int idGame, String uuidPlayer) {
		
		//saves the game ID and the player ID
		this.idGame = idGame;
		this.uuidPlayer = uuidPlayer;
		
		//calculates the throws and store them in temporal variables
		//[(random number between 1 and 0) * (6 numbers that has a die)] + (1 has to be added as the result is between 0 and 5)
		die1Result = (int) (Math.random() * 6) + 1; 
		die2Result = (int) (Math.random() * 6) + 1;
		totalResult = die1Result + die2Result;
		if (totalResult == 7) {
			win = true;
		}
		else {
			win = false;
		}
	}


	
	public int getDie1Result() {
		return die1Result;
	}
	public void setDie1Result(int die1Result) {
		this.die1Result = die1Result;
	}
	public int getDie2Result() {
		return die2Result;
	}
	public void setDie2Result(int die2Result) {
		this.die2Result = die2Result;
	}
	public double getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	public int getIdGame() {
		return idGame;
	}
	public void setIdGame(int idGame) {
		this.idGame = idGame;
	}
	public String getUuidPlayer() {
		return uuidPlayer;
	}
	public void setIdPlayer(String uuidPlayer) {
		this.uuidPlayer = uuidPlayer;
	}
	public boolean isWin() {
		return win;
	}
	public void setWin(boolean win) {
		this.win = win;
	}



	@Override
	public String toString() {
		return "\"idGame" + idGame + "\": {\"die1Result\":" + die1Result + ", \"die2Result\":" + die2Result
				+ ", \"totalResult\":" + totalResult + ", \"win\":" + win + ", \"idPlayer\":" + uuidPlayer + "}";
	}
}
