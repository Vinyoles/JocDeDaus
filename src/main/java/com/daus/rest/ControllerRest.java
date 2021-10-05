package com.daus.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daus.models.Dice;
import com.daus.models.IdToAssign;
import com.daus.models.User;
import com.daus.repository.GamesRepository;
import com.daus.repository.IdsRepository;
import com.daus.repository.UsersRepository;

@CrossOrigin(origins = "http://127.0.0.1:5500", maxAge = 3600) //TODO canviar en funció de la web des d'on es conecti
@RestController
@RequestMapping("/players")
public class ControllerRest {
	
	//injection of the dependencies
	@Autowired
	private UsersRepository usersRepo;
	
	@Autowired
	private IdsRepository idRepo;
	
	@Autowired
	private GamesRepository gamesRepo;
	
	
	//create a new player
	@PostMapping
	public String createNewUser(@RequestBody User user) {
		
		//finds the last ID and sends it to the constructor
		int newId;
		Optional<IdToAssign> idOptional = idRepo.findById(1);
		if(idOptional.isPresent()) {
			newId = idOptional.get().getIdStored() + 1;
			idOptional.get().setIdStored(newId);
			idRepo.save(idOptional.get());
		}
		else {
			newId = 1;
			IdToAssign createId = new IdToAssign(1, newId);
			idRepo.save(createId);
		}
		user.assignId(newId);
		
		//assigns the date and save the result to the database
		user.assignLocalDate();
		usersRepo.save(user);
		return "User created (ID-Name): " + user.getID() + "-" + user.getName();
	}
	
	//returns all the players
	@GetMapping
	public List<User> getAllUsers() {
		List<User> users = usersRepo.findAll();
		return users;
	}
	
	//modifies a player name
	@PostMapping("/{id}")
	public String modifyUser(@PathVariable int id, @RequestBody String userName) {
		Optional<User> optionalUser = usersRepo.findById(id);
		if(optionalUser.isPresent()) {
			optionalUser.get().setName(userName);
			usersRepo.save(optionalUser.get());
			return "New name for the user " + id + ": " + userName;
		}
		else {
			return "User not found";
		}
	}
	
	
	@PostMapping("/{idPlayer}/games")
	public String throwDices(@PathVariable int idPlayer) {
		Optional<User> optionalUser = usersRepo.findById(idPlayer);
		if(optionalUser.isPresent()) {
			
			//Calculates the game id
			int idGame;
			Optional<IdToAssign> idOptional = idRepo.findById(2);
			if(idOptional.isPresent()) {
				idGame = idOptional.get().getIdStored() + 1;
				idOptional.get().setIdStored(idGame);
				idRepo.save(idOptional.get());
			}
			else {
				idGame = 1;
				IdToAssign createId = new IdToAssign(2, idGame);
				idRepo.save(createId);
			}
			
			//throw the dices
			Dice diceResult = new Dice();
			diceResult.throwDice(idGame, idPlayer);
			
			//save the result to the DB
			gamesRepo.save(diceResult);
			return diceResult.toString();
		}
		else {
			return "User not found";
		}
	}
}


/*
* POST: /players : crea un jugador 
* PUT /players : modifica el nom del jugador 
* POST /players/{id}/games/ : un jugador específic realitza una tirada dels daus.  
DELETE /players/{id}/games: elimina les tirades del jugador 
GET /players/: retorna el llistat de tots els jugadors del sistema amb el seu percentatge mig d’èxits   
GET /players/{id}/games: retorna el llistat de jugades per un jugador.  
GET /players/ranking: retorna el ranking mig de tots els jugadors del sistema. És a dir, el percentatge mig d’èxits. 
GET /players/ranking/loser: retorna el jugador amb pitjor percentatge d’èxit 
GET /players/ranking/winner: retorna el jugador amb pitjor percentatge d’èxit 
 * */

