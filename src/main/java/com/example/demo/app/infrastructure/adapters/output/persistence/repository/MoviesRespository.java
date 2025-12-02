package com.example.demo.app.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.app.infrastructure.adapters.output.persistence.entity.MoviesEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoviesRespository extends JpaRepository<MoviesEntity, Long> {

    // Buscar película por título exacto
    Optional<MoviesEntity> findByTitulo(String titulo);

    // Buscar películas por género
    List<MoviesEntity> findByGenero(String genero);

    // Buscar películas que contengan cierta palabra en el título (insensible a mayúsculas/minúsculas)
    List<MoviesEntity> findByTituloContainingIgnoreCase(String titulo);

    // Verificar si existe una película por título
    boolean existsByTitulo(String titulo);
}
