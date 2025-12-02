package com.example.demo.app.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;

import com.example.demo.app.domain.model.User;
import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // DOMAIN -> ENTITY
    UserEntity toEntity(User domain);

    // ENTITY -> DOMAIN
    User toDomain(UserEntity entity);
}
