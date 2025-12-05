package com.example.demo.app.aplication.port.out;

import com.example.demo.app.domain.model.User;
import com.example.demo.app.domain.model.UserRole;



public interface UserPersistencePort {
	/*
	 * 
	 * >>>>>>> IMPLEMENTED BY USER JPA ADAPTER<<<<<<<<<<
	 * 
	 */
    User save(User user);
    
	void saveUserRole(UserRole userRole);
    
}
