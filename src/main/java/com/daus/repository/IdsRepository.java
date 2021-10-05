package com.daus.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daus.models.IdToAssign;

public interface IdsRepository extends MongoRepository<IdToAssign, Integer> {

}
