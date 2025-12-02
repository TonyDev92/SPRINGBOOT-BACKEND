package com.example.demo.app.infrastructure.adapters.output.persistence.implementations;

import org.springframework.stereotype.Component;

import com.example.demo.app.aplication.port.out.UserPersistencePort;
import com.example.demo.app.domain.model.User;
import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserEntity;
import com.example.demo.app.infrastructure.adapters.output.persistence.mapper.UserMapper;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.UserRepository;

@Component
public class UserJpaAdapter implements UserPersistencePort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserJpaAdapter(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
}
