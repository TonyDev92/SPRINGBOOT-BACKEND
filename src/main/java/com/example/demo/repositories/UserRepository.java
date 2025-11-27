package com.example.demo.repositories;

import com.example.demo.entities.UserEntitie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntitie, Long> {

    // Buscar usuario por username
    Optional<UserEntitie> findByUsername(String username);

    // Buscar usuario por email
    Optional<UserEntitie> findByEmail(String email);

    // Verificar si existe un usuario por username
    boolean existsByUsername(String username);

    // Verificar si existe un usuario por email
    boolean existsByEmail(String email);
}
	