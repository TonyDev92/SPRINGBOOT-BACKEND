	package com.example.demo.app.infrastructure.adapters.output.persistence.implementations;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.app.aplication.port.out.UserPersistencePort;
import com.example.demo.app.domain.model.User;
import com.example.demo.app.domain.model.UserRole;
import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserEntity;
import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserRolesEntity;
import com.example.demo.app.infrastructure.adapters.output.persistence.mapper.UserMapper;
import com.example.demo.app.infrastructure.adapters.output.persistence.mapper.UserRoleMapper;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.UserRepository;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.UserRolesRepository;

@Component
public class UserJpaAdapter implements UserPersistencePort {
	
	/*
	 * 
	 * >>>>>>USE USER REPOSITORY<<<<<<
	 * 
	 * >>>>>>USE USER MAPPPER<<<<<<<<<
	 * 
	 */
	@Autowired
    private  UserRepository userRepository;
	@Autowired
    private  UserMapper userMapper;
	@Autowired
    private  UserRoleMapper userRoleMapper;
	@Autowired
    private  UserRolesRepository userRolesRepository;

    @Override
    public User save(User user) {

        UserEntity entity = userMapper.toEntity(user);

        UserEntity savedEntity = userRepository.save(entity);

        return userMapper.toDomain(savedEntity);
    }
    
    @Override
    public void saveUserRole(UserRole userRole) {
      
        UserRolesEntity entity = userRoleMapper.toEntity(userRole);

        if (entity.getAssignedAt() == null) {
            entity.setAssignedAt(LocalDateTime.now());
        }

        userRolesRepository.save(entity);
    }

}
