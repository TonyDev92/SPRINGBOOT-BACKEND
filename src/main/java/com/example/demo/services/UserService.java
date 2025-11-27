package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.UserEntitie;
import com.example.demo.repositories.UserRepository;

@Service
public class UserService {
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(BCryptPasswordEncoder passwordEncoder) {
    	this.passwordEncoder = passwordEncoder;
    }
    public boolean checkPassword(String rawPassword, String encodedPassword) {
    	//rawPassword -> contraseña introducida por usuario
    	//encodedPassword -> Contraseña hasheada en BBDD
    	return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public UserEntitie createUser(String username, String email, String password) {
        UserEntitie user = new UserEntitie();
        user.setUsername(username);
        user.setEmail(email);                   // obligatorio
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setStatus((byte)1);                // activo por defecto
        return userRepository.save(user);
    }
}
