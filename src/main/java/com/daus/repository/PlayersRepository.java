package com.daus.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daus.models.Player;

public interface PlayersRepository extends MongoRepository<Player, Integer> {

}
