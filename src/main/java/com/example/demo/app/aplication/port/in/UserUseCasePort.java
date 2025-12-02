package com.example.demo.app.aplication.port.in;

import org.springframework.security.core.Authentication;

import com.example.demo.app.domain.model.User;


public interface UserUseCasePort {

    
    public boolean checkPassword(String rawPassword, String encodedPassword);

    public User createUser(String username, String email, String password);
    
    public String login(String error, String logout,String csrfToken, Authentication auth);
    
   
}	
	
