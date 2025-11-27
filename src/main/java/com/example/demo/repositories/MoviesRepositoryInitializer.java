package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Movies;

import jakarta.transaction.Transactional;



@Repository
public interface MoviesRepositoryInitializer extends JpaRepository<Movies, Long>{
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO Peliculas (Titulo , Descripcion, Reparto , Imagen, NombreImagen, Genero, FechaEstreno , DuracionMinutos)"
			+ "VALUES (:titulo, :descripcion, :reparto , :imagen, :nombreImagen, :genero, :fechaEstreno, :duracionMinutos)", nativeQuery = true)
	void insertMovie(String titulo,String descripcion, String reparto, byte[] imagen, String nombreImagen, String genero, Object fechaEstreno, Integer duracionMinutos );
	
	
}