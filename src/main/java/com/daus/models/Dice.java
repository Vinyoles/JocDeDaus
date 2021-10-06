package com.daus.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class Dice {
	 
	@Id
	private int idGame;
	
	@Field(name="dice1")
	private int dice1Result;
	
	@Field(name="dice2")
	private int dice2Result;
	
	@Field(name="totalResult")
	private int totalResult;
	
	@Field(name="idPlayer")
	private int idPlayer;

	
	
	//returns the value of a dice throw
	public void throwDice(int idGame, int idPlayer) {
		
		//saves the game ID and the player ID
		this.idGame = idGame;
		this.idPlayer = idPlayer;
		
		//calculates the throws and store them in temporal variables
		//[(random number between 1 and 0) * (6 numbers that has a dice)] + (1 has to be added as the result is between 0 and 5)
		dice1Result = (int) (Math.random() * 6) + 1; 
		dice2Result = (int) (Math.random() * 6) + 1;
		totalResult = dice1Result + dice2Result;
	}


	
	
	public int getDice1Result() {
		return dice1Result;
	}
	public void setDice1Result(int dice1Result) {
		this.dice1Result = dice1Result;
	}
	public int getDice2Result() {
		return dice2Result;
	}
	public void setDice2Result(int dice2Result) {
		this.dice2Result = dice2Result;
	}
	public int getTotalResult() {
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
	public int getIdPlayer() {
		return idPlayer;
	}
	public void setIdPlayer(int idPlayer) {
		this.idPlayer = idPlayer;
	}


	@Override
	public String toString() {
		return "\"idGame" + idGame + "\": {\"dice1Result\":" + dice1Result + ", \"dice2Result\":" + dice2Result
				+ ", \"totalResult\":" + totalResult + ", \"idPlayer\":" + idPlayer + "}";
	}
}
