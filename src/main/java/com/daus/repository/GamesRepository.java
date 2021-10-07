package com.daus.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daus.models.Game;

public interface GamesRepository extends MongoRepository<Game, Integer>{

}
