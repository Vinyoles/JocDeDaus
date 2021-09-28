package com.daus.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.daus.dao.UsersDAO;
import com.daus.models.User;

@CrossOrigin(origins = "http://127.0.0.1:5500", maxAge = 3600) //TODO canviar en funció de la web des d'on es conecti
@RestController("/players")
public class ControllerRest {
	
	//injection of the dependencies
	@Autowired
	private UsersDAO usersDAO;
	
	//create a new player
	@PostMapping
	public ResponseEntity<User> createNewUser(@RequestBody User user) {
		User newUser = usersDAO.save(user);
		return ResponseEntity.ok(newUser);
	}
	

}



/*
POST: /players : crea un jugador 
PUT /players : modifica el nom del jugador 
POST /players/{id}/games/ : un jugador específic realitza una tirada dels daus.  
DELETE /players/{id}/games: elimina les tirades del jugador 
GET /players/: retorna el llistat de tots els jugadors del sistema amb el seu percentatge mig d’èxits   
GET /players/{id}/games: retorna el llistat de jugades per un jugador.  
GET /players/ranking: retorna el ranking mig de tots els jugadors del sistema. És a dir, el percentatge mig d’èxits. 
GET /players/ranking/loser: retorna el jugador amb pitjor percentatge d’èxit 
GET /players/ranking/winner: retorna el jugador amb pitjor percentatge d’èxit 
 * */
