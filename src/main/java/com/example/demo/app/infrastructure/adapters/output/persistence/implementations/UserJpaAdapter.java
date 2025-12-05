	package com.example.demo.app.infrastructure.adapters.output.persistence.implementations;

import java.time.LocalDateTime;

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

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserRolesRepository userRolesRepository;

    public UserJpaAdapter(UserRepository userRepository, UserMapper userMapper , UserRolesRepository userRolesRepository,UserRoleMapper userRoleMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userRolesRepository = userRolesRepository;
        this.userRoleMapper = userRoleMapper;
    }

//    @Override
//    public UserEntity save(UserEntity user) {
//        return userRepository.save(user);
//    }
   

    @Override
    public User save(User user) {

        // dominio → entidad
        UserEntity entity = userMapper.toEntity(user);

        // guardar en JPA
        UserEntity savedEntity = userRepository.save(entity);

        // entidad → dominio
        return userMapper.toDomain(savedEntity);
    }
    
//    @Override
//    public void saveUserRole(UserRole userRole) {
//        // Si AssignedAt es null, asignamos la fecha actual
//        if (userRole.getAssignedAt() == null) {
//            userRole.setAssignedAt(LocalDateTime.now());
//        }
//        userRolesRepository.save(userRole);
//    }
    @Override
    public void saveUserRole(UserRole userRole) {
        // Convertir dominio -> entidad
        UserRolesEntity entity = userRoleMapper.toEntity(userRole);

        // Asignar fecha si no tiene
        if (entity.getAssignedAt() == null) {
            entity.setAssignedAt(LocalDateTime.now());
        }

        // Guardar en JPA
        userRolesRepository.save(entity);
    }

}
