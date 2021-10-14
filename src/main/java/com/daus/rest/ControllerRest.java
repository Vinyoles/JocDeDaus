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
import com.daus.models.Player;
import com.daus.repository.GamesRepository;
import com.daus.repository.IdsRepository;
import com.daus.repository.PlayersRepository;

@CrossOrigin(origins = "http://127.0.0.1:5500", maxAge = 3600) //TODO canviar en funció de la web des d'on es conecti
@RestController
@RequestMapping("/players")
public class ControllerRest {
	
	//injection of the dependencies
	@Autowired
	private PlayersRepository playersRepo;
	
	@Autowired
	private IdsRepository idRepo;
	
	@Autowired
	private GamesRepository gamesRepo;
	
	
	//create a new player
	@PostMapping
	public String createNewPlayer(@RequestBody Player player) {
		
		//assigns the UUID
		player.assignUUID();
		
		//assigns the date and save the result to the database
		player.assignLocalDate();
		playersRepo.save(player);
		return "Player created (ID-Name-Anonymous): " + player.getUuid() + "-" + player.getPlayerName() + "-" + player.isAnonymous();
	}
	
	//returns all the players and its mean exit percentage
	@GetMapping
	public String getAllPlayers() {
		
		//list all the players and games
		List<Game> games = gamesRepo.findAll();
		List<Player> players = playersRepo.findAll();
		
		//declares and initiates the return string
		String playersWithPercentage = "{\"players\":{";
		
		//for each player and game, stores its variable to a JSON file
		int remainingPlayers = players.size()-1;
		for (Player player: players) {
			playersWithPercentage += player.toString();
			
			//stores all the games from a player
			ArrayList<Game> playerGames = new ArrayList<Game>();
			Iterator<Game> ite = games.iterator();
			while(ite.hasNext()) {
				Game currentGame = ite.next();
				
				if (currentGame.getUuidPlayer().equals(player.getUuid())) {
					playerGames.add(currentGame);
				}
			}
			
			//adds each of the player games to the JSON file
			int remainingPlayerGames = -1;
			for(Game game: playerGames) {
				playersWithPercentage += game.toString();
				
				//adds a comma to the JSON file for separating from the next data
				if (remainingPlayerGames == -1) {
					remainingPlayerGames = playerGames.size()-1;
				}
				if (remainingPlayerGames > 0) {
					playersWithPercentage += ", ";
					remainingPlayerGames--;
				}
			}
			
			//stores to the JSON the player success rate. If the player hasn't played any game, stores 0
			playersWithPercentage += "}, \"meanValue\":" + player.meanValue(games) + "}";
			
			//adds a comma if there are more players
			if (remainingPlayers > 0) {
				playersWithPercentage += ", ";
				remainingPlayers--;
			}
		}
		
		//encloses the string and returns the JSON data
		playersWithPercentage += "}}";
		return playersWithPercentage;
	}
	
	//modifies a player name
	@PostMapping("/{uuid}")
	public String modifyPlayer(@PathVariable String uuid, @RequestBody Player player) {
		Optional<Player> optionalPlayer = playersRepo.findById(uuid);
		if(optionalPlayer.isPresent()) {
			if (optionalPlayer.get().getPlayerName() != null) {
				optionalPlayer.get().setPlayerName(player.getPlayerName());
			}
			optionalPlayer.get().setAnonymous(player.isAnonymous());
			playersRepo.save(optionalPlayer.get());
			return "PlayerName-Anonymous: " + player.getPlayerName() + "-" + player.isAnonymous();
		}
		else {
			return "Player not found";
		}
	}
	
	//Makes a game throw for a player
	@PostMapping("/{uuidPlayer}/games")
	public String playGame(@PathVariable String uuidPlayer) {
		Optional<Player> optionalPlayer = playersRepo.findById(uuidPlayer);
		if(optionalPlayer.isPresent()) {
			
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
			gameResult.playGame(idGame, uuidPlayer);
			
			//save the result to the DB
			gamesRepo.save(gameResult);
			return gameResult.toString();
		}
		else {
			return "Player not found";
		}
	}
	
	//Deletes all the throws from a player
	@DeleteMapping("/{idPlayer}/games")
	public String deleteGames(@PathVariable String idPlayer) {
		
		Optional<Player> optionalPlayer = playersRepo.findById(idPlayer);
		if(optionalPlayer.isPresent()) {
			
			//collects all the games
			List<Game> allGames = gamesRepo.findAll();
			
			//iterates along all the games to detect the games made by a player and deletes it
			Iterator<Game> ite = allGames.iterator();
			while(ite.hasNext()) {
				Game game = ite.next();
				if (game.getUuidPlayer().equals(idPlayer)) {
					int idDelete = game.getIdGame();
					gamesRepo.deleteById(idDelete);
				}
			}
			return "Games history cleared";
		}
		else {
			return "Player not found";
		}
	}
	
	//gets all the games from a player
	@GetMapping("/{idPlayer}/games")
	public String gamesFromPlayerId(@PathVariable String idPlayer) {
		Optional<Player> optionalPlayer = playersRepo.findById(idPlayer);
		if(optionalPlayer.isPresent()) {
			
			//collects all the games
			List<Game> allGames = gamesRepo.findAll();
			String toReturn = "{";
			
			//iterates along all the games to detect the games made by a player and stores it
			Iterator<Game> ite = allGames.iterator();
			int remainingGames = -1;
			ArrayList<Integer> idGames = new ArrayList<Integer>();
			
			while(ite.hasNext()) {
				Game game = ite.next();
				
				//collects all the games IDs that are played by a player
				if (game.getUuidPlayer().equals(idPlayer)) {
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
			return "Player not found";
		}
	}
	
	//returns the percentage of all the players on the system
	@GetMapping("/ranking")
	public String getRanking() {
		String ranking = "{\"Success percentage\":";
		
		//for all the games, searches for the success and the failed games
		int success = 0;
		int fail = 0;
		List<Game> games = gamesRepo.findAll();
		for (Game game: games) {
			if (game.isWin()) success++;
			else fail++;
		}
		
		//calculates the result from the data on success and failure
		double result;
		if (fail == 0 && success != 0) result = 100;
		else if (fail == 0 && success == 0) result = 0;
		else result = (double) success/(success + fail) * 100;
		
		//returns the result
		ranking += result +"}";
		return ranking;
	}
	
	//returns the player with the lower percentage
	@GetMapping("/ranking/loser")
	public String getLoser() {
		String loser = "{";
		List<Game> games = gamesRepo.findAll();
		List<Player> players = playersRepo.findAll();
		Player theLoser = null;
		
		//searches from the players, the player with a lower games rates
		for (Player player: players) {
			double percentage = player.winPercentage(games);

			//searches for the lower win percentage on a player
			try {
				if (theLoser == null && percentage != 0) {
					theLoser = player;
				}
				else if (percentage < theLoser.winPercentage(games) && percentage != 0) {
					theLoser = player;
				}
			}
			catch (Exception nullPointerException){
				System.out.println("theLoser parameter is null");
			}
		}
		
		//returns the loser, if non of the players has won a game, returns a message
		if (theLoser == null) {
			return "Non of the players has won a game. If the players only had losed games, does not enter on the ranking";
		}
		else {
			//saves the player name and mean value and returns the result
			String loserName = theLoser.getPlayerName();
			double loserValue = theLoser.winPercentage(games);
			loser += "\"loser name\":" + loserName + ", \"Success percentage\":" + loserValue + "}";
			return loser;
		}
	}
	
	//returns the player with the higher percentage
	@GetMapping("/ranking/winner")
	public String getWinner() {
		String winner = "{";
		List<Game> games = gamesRepo.findAll();
		List<Player> players = playersRepo.findAll();
		Player theWinner = null;
		
		//searches from the players, the player with a higher games rates
		for (Player player: players) {
			double percentage = player.winPercentage(games);

			//searches for the higher win percentage on a player
			try {
				if (theWinner == null && percentage != 0) {
					theWinner = player;
				}
				else if (percentage > theWinner.winPercentage(games)) {
					theWinner = player;
				}
			}
			catch (Exception nullPointerException){
				System.out.println("theWinner parameter is null");
			}
		}
		
		//returns the winner, if non of the players has won a game, returns a message
		if (theWinner == null) {
			return "Non of the players has won a game";
		}
		else {
			//saves the player name and mean value and returns the result
			String winnerName = theWinner.getPlayerName();
			double winnerValue = theWinner.winPercentage(games);
			winner += "\"winner name\":" + winnerName + ", \"Success percentage\":" + winnerValue + "}";
			return winner;
		}
	}
}



