package com.daus.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daus.models.User;

public interface UsersDAO extends JpaRepository<User, Integer> {

}
