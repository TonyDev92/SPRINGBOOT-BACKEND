package com.example.demo.app.aplication.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.app.aplication.port.in.UserUseCasePort;
import com.example.demo.app.aplication.port.out.UserPersistencePort;
import com.example.demo.app.domain.model.User;
import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserEntity;




@Service
public class UserAplicationService implements UserUseCasePort{
	
 
    
    @Autowired
    private UserPersistencePort userPersistencePort;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public UserAplicationService(BCryptPasswordEncoder passwordEncoder) {
    	this.passwordEncoder = passwordEncoder;
    }
    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
    	//rawPassword -> contraseña introducida por usuario
    	//encodedPassword -> Contraseña hasheada en BBDD
    	return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public User createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setStatus((byte)1);

        return userPersistencePort.save(user); 
    }
    
    @Override
    public String login(String error, String logout, String csrfToken, Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/private/home";
        }
        return null;
    }
}
