package com.example.demo.repositories;

import com.example.demo.entities.Movies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoviesRespository extends JpaRepository<Movies, Long> {

    // Buscar película por título exacto
    Optional<Movies> findByTitulo(String titulo);

    // Buscar películas por género
    List<Movies> findByGenero(String genero);

    // Buscar películas que contengan cierta palabra en el título (insensible a mayúsculas/minúsculas)
    List<Movies> findByTituloContainingIgnoreCase(String titulo);

    // Verificar si existe una película por título
    boolean existsByTitulo(String titulo);
}
