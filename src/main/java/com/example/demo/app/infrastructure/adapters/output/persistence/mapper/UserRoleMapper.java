package com.example.demo.app.infrastructure.adapters.output.persistence.mapper;

import org.mapstruct.Mapper;


import com.example.demo.app.domain.model.UserRole;
import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserRolesEntity;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    // DOMAIN -> ENTITY
    UserRolesEntity toEntity(UserRole domain);

    // ENTITY -> DOMAIN
    UserRole toDomain(UserRolesEntity entity);
}
