package com.example.demo.app.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Buscar usuario por username
    Optional<UserEntity> findByUsername(String username);

    // Buscar usuario por email
    Optional<UserEntity> findByEmail(String email);

    // Verificar si existe un usuario por username
    boolean existsByUsername(String username);

    // Verificar si existe un usuario por email
    boolean existsByEmail(String email);
}
	