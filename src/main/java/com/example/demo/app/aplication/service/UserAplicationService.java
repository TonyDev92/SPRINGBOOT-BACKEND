package com.example.demo.app.aplication.service;

import java.time.LocalDateTime;



import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.app.aplication.port.in.UserUseCasePort;
import com.example.demo.app.aplication.port.out.UserPersistencePort;
import com.example.demo.app.domain.model.User;
import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserRolesEntity;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.UserRolesRepository;



@Service
public class UserAplicationService implements UserUseCasePort{
	
	
	/*
	 * 
	 * >>>>>>USE CASE<<<<<<<<<
	 * >>>>>>CALL PORT OUT<<<<
	 * 
	 * 
	 */
    
	@Autowired
	private UserRolesRepository userRolesRepository;
	
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
    public User createUser(String username, String email, String password, String csrfToken) {
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setStatus((byte) 1);

       
        User savedUser = userPersistencePort.save(user);
        
        
        UserRolesEntity userRoleEntity = new UserRolesEntity();
        userRoleEntity.setIdUsuario(savedUser.getId());
        userRoleEntity.setIdRol(1L); 
        userRoleEntity.setAssignedAt(LocalDateTime.now());
        userRoleEntity.setRole("ROLE_USER"); 
        userRolesRepository.save(userRoleEntity);

        
//        UserRole userRole = new UserRole();
//        System.out.println("=====USER ID========" + savedUser.getId());
//        userRole.setUserId(savedUser.getId());
//        userRole.setRoleName("ROLE_USER");
//        userRole.setAssignedAt(LocalDateTime.now());
//
//    
//        userPersistencePort.saveUserRole(userRole);

        return savedUser;
    }
    
    @Override
    public String login(String error, String logout, String csrfToken, Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/private/home";
        }
        return null;
    }
	
}
