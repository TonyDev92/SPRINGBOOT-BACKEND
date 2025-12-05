package com.example.demo.app.infrastructure.adapters.output.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserRolesEntity;



@Repository
public interface UserRolesRepository extends JpaRepository<UserRolesEntity, Long> {

   
    List<UserRolesEntity> findByIdUsuario(Long idUsuario);
    
  
}
