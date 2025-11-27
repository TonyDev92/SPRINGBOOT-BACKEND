package com.example.demo.services;

import org.springframework.stereotype.Service;
import com.example.demo.repositories.UserTokenRepository;
import jakarta.servlet.http.HttpSession;

@Service
public class LogoutService {

    private final UserTokenRepository tokenRepository;

    public LogoutService(UserTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void logout(HttpSession session, Long userId) {
        if (session == null) return;

        Object tokenObj = session.getAttribute("userToken");
        try {
            // Eliminar token individual si está en sesión
            if (tokenObj != null) {
                String token = tokenObj.toString();
                tokenRepository.deleteByToken(token);
            }

            // O eliminar todos los tokens del usuario
            if (userId != null) {
                tokenRepository.deleteByUserId(userId);
            }

        } catch (Exception ex) {
            System.err.println("Error eliminando token en DB: " + ex.getMessage());
        }

        // Limpiar sesión
        session.removeAttribute("userToken");
        session.invalidate();
    }
}


