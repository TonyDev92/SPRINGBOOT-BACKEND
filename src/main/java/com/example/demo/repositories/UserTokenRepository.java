package com.example.demo.repositories;

import com.example.demo.entities.UserToken;
import jakarta.transaction.Transactional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    List<UserToken> findByIdUsuarioAndIsActive(Long idUsuario, Boolean isActive);

    // Elimina un token específico
    @Modifying
    @Transactional
    @Query("DELETE FROM UserToken u WHERE u.token = :token")
    int deleteByToken(String token);

    // Elimina todos los tokens de un usuario (opcional)
    @Modifying
    @Transactional
    @Query("DELETE FROM UserToken u WHERE u.idUsuario = :userId")
    int deleteByUserId(Long userId);
}
