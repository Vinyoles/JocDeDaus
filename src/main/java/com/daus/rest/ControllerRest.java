package com.daus.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daus.models.Game;
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
	
	//returns all the players and its mean exit percentage
	@GetMapping
	public String getAllUsers() {
		
		//list all the users and games
		List<Game> games = gamesRepo.findAll();
		List<User> users = usersRepo.findAll();
		
		//declares and initiates the return string
		String usersWithPercentage = "{\"users\":{";
		
		//for each user and game, stores its variable to a JSON file
		int remainingUsers = users.size()-1;
		for (User user: users) {
			usersWithPercentage += user.toString();
			int numberOfGames = 0;
			int addedResults = 0;
			
			//stores all the games from a player
			ArrayList<Game> playerGames = new ArrayList<Game>();
			Iterator<Game> ite = games.iterator();
			while(ite.hasNext()) {
				Game currentGame = ite.next();
				
				if (currentGame.getIdPlayer() == user.getID()) {
					playerGames.add(currentGame);
				}
			}
			
			//adds each of the player games to the JSON file
			int remainingPlayers = -1;
			for(Game game: playerGames) {
				usersWithPercentage += game.toString();
				addedResults += game.getTotalResult();
				numberOfGames++;
				
				//adds a comma to the JSON file for separating from the next data
				if (remainingPlayers == -1) {
					remainingPlayers = playerGames.size()-1;
				}
				if (remainingPlayers > 0) {
					usersWithPercentage += ", ";
					remainingPlayers--;
				}
			}
			
			//calculates and stores to the JSON the player success rate. If the player hasn't played any game, returns 0
			double userPercentage;
			if (numberOfGames > 0) {
				userPercentage = addedResults / numberOfGames;
			}
			else {
				userPercentage = 0;
			}
			usersWithPercentage += "}, \"meanValue\":" + userPercentage + "}";
			
			//adds a comma if there are more users
			if (remainingUsers > 0) {
				usersWithPercentage += ", ";
				remainingUsers--;
			}
		}
		
		//encloses the string and returns the JSON data
		usersWithPercentage += "}}";
		return usersWithPercentage;
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
	
	//Makes a game throw for a player
	@PostMapping("/{idPlayer}/games")
	public String playGame(@PathVariable int idPlayer) {
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
			
			//throw the dice
			Game gameResult = new Game();
			gameResult.startGame(idGame, idPlayer);
			
			//save the result to the DB
			gamesRepo.save(gameResult);
			return gameResult.toString();
		}
		else {
			return "User not found";
		}
	}
	
	//Deletes all the throws from a player
	@DeleteMapping("/{idPlayer}/games")
	public String deleteGames(@PathVariable int idPlayer) {
		
		Optional<User> optionalUser = usersRepo.findById(idPlayer);
		if(optionalUser.isPresent()) {
			
			//collects all the games
			List<Game> allGames = gamesRepo.findAll();
			
			//iterates along all the games to detect the games made by a player and deletes it
			Iterator<Game> ite = allGames.iterator();
			while(ite.hasNext()) {
				Game game = ite.next();
				if (game.getIdPlayer() == idPlayer) {
					int idDelete = game.getIdGame();
					gamesRepo.deleteById(idDelete);
				}
			}
			return "Games history cleared";
		}
		else {
			return "User not found";
		}
	}
	
	//gets all the games from a player //TODO formatejar el JSON
	@GetMapping("/{idUser}/games")
	public String gamesFromPlayerId(@PathVariable int idUser) {
		Optional<User> optionalUser = usersRepo.findById(idUser);
		if(optionalUser.isPresent()) {
			
			//collects all the games
			List<Game> allGames = gamesRepo.findAll();
			String toReturn = "{";
			
			//iterates along all the games to detect the games made by a player and stores it
			Iterator<Game> ite = allGames.iterator();
			int remainingGames = -1;
			ArrayList<Integer> idGames = new ArrayList<Integer>();
			
			while(ite.hasNext()) {
				Game game = ite.next();
				
				//collects all the games IDs that are played by a user
				if (game.getIdPlayer() == idUser) {
					idGames.add(game.getIdGame());
				}
			}
			
			//adds the different games to the JSON
			for (int i=0; i<idGames.size(); i++) {
				toReturn += gamesRepo.findById(idGames.get(i)).get().toString();
				
				//adds a comma to the JSON file for separating from the next data
				if (remainingGames == -1) {
					remainingGames = idGames.size()-1;
				}
				if (remainingGames > 0) {
					toReturn += ", ";
					remainingGames--;
				}
			}
			
			//encloses the JSON file and returns the result
			toReturn += "}";
			return toReturn;
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
* DELETE /players/{id}/games: elimina les tirades del jugador 
* GET /players/: retorna el llistat de tots els jugadors del sistema amb el seu percentatge mig d’èxits   
* GET /players/{id}/games: retorna el llistat de jugades per un jugador.  
GET /players/ranking: retorna el ranking mig de tots els jugadors del sistema. És a dir, el percentatge mig d’èxits. 
GET /players/ranking/loser: retorna el jugador amb pitjor percentatge d’èxit 
GET /players/ranking/winner: retorna el jugador amb millor percentatge d’èxit 
 * */

