package com.example.demo.app.infrastructure.adapters.output.persistence.repository;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserTokenEntity;



@Repository
public interface UserTokenRepository extends JpaRepository<UserTokenEntity, Long> {

    List<UserTokenEntity> findByIdUsuarioAndIsActive(Long idUsuario, Boolean isActive);

    // Elimina un token específico
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM UserTokens WHERE Token = :token" , nativeQuery = true)
    int deleteByToken(String token);

    // Elimina todos los tokens de un usuario (opcional)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM UserTokens WHERE SessionId = :sessionId", nativeQuery = true)
    int deleteBySessionId(String sessionId);
}
