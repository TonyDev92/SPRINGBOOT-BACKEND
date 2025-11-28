package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.example.demo.entities.UserEntity;

import jakarta.transaction.Transactional;


@Repository
public interface UserRepositoryInitialazer extends JpaRepository<UserEntity, Long> {
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO Usuarios (Username, Email, PasswordHash, Status) "
			+ "VALUES (:username, :userEmail, :hashPassword, :Status)", nativeQuery = true)
    void insertUser(String username , String userEmail ,String hashPassword , String Status);
    
    
}


