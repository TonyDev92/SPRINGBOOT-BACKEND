package com.example.demo.app.aplication.port.in;

import org.springframework.security.core.Authentication;

import com.example.demo.app.domain.model.User;


public interface UserUseCasePort {

    /*
     * 
     * 
     * >>>>>>USE CASE -> APLICATION SERVICE<<<<<<<<
     * 
     * 
     */
    public boolean checkPassword(String rawPassword, String encodedPassword);
    
    public String login(String error, String logout,String csrfToken, Authentication auth);

	public User createUser(String username, String email, String password, String csrfToken);
    
   
}	
	
