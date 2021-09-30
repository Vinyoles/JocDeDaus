package com.daus.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daus.models.User;

public interface UsersRepository extends MongoRepository<User, Integer> {

}
