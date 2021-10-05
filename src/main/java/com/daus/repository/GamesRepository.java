package com.daus.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daus.models.Dice;

public interface GamesRepository extends MongoRepository<Dice, Integer>{

}
