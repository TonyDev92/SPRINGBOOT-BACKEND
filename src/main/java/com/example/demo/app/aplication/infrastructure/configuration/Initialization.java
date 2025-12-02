package com.example.demo.app.aplication.infrastructure.configuration;

import org.springframework.beans.factory.InitializingBean;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.app.aplication.infrastructure.common.PeliculaDTO;
import com.example.demo.app.aplication.infrastructure.common.UsuarioDTO;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.MoviesRepositoryInitializer;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.MoviesRespository;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.UserRepository;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.UserRepositoryInitialazer;
import com.example.demo.services.csvService;

import jakarta.transaction.Transactional;

import java.util.List;

@Component
public class Initialization implements InitializingBean {

    private final UserRepositoryInitialazer initUser;
    private final MoviesRepositoryInitializer initMovies;
    private final BCryptPasswordEncoder passwordEncoder;
    private final csvService csvReaderService;

    private final UserRepository userRepository;
    private final MoviesRespository moviesRepository;

    @Value("classpath:csv/usuarios.csv")
    private Resource usuariosCsv;

    @Value("classpath:csv/peliculas.csv")
    private Resource peliculasCsv;

    public Initialization(UserRepositoryInitialazer initUser,
                          MoviesRepositoryInitializer initMovies,
                          BCryptPasswordEncoder passwordEncoder,
                          csvService csvReaderService,
                          UserRepository userRepository,
                          MoviesRespository moviesRepository) {
        this.initUser = initUser;
        this.initMovies = initMovies;
        this.passwordEncoder = passwordEncoder;
        this.csvReaderService = csvReaderService;
        this.userRepository = userRepository;
        this.moviesRepository = moviesRepository;
    }

    @Override
    @Transactional
    public void afterPropertiesSet() throws Exception {
        List<UsuarioDTO> usuarios = csvReaderService.readUsers(usuariosCsv);
        List<PeliculaDTO> peliculas = csvReaderService.readMovies(peliculasCsv);

        // Insert Users
        for (UsuarioDTO u : usuarios) {
            if (userRepository.existsByUsername(u.getUsername())) {
                System.out.println("No se han introducido los siguientes datos de usuario: " + u.getUsername());
            } else {
                String passwordEncoded = passwordEncoder.encode(u.getPassword());
                initUser.insertUser(u.getUsername(), u.getEmail(), passwordEncoded, u.getStatus().toString());
            }
        }

        // Insert Movies
        for (PeliculaDTO p : peliculas) {
            if (moviesRepository.existsByTitulo(p.getTitulo())) {
                System.out.println("No se han introducido los siguientes datos de película: " + p.getTitulo());
            } else {
                initMovies.insertMovie(
                        p.getTitulo(),
                        p.getDescripcion(),
                        p.getReparto(),
                        p.getImagen(),
                        p.getNombreImagen(),
                        p.getGenero(),
                        p.getFechaEstreno(),
                        p.getDuracion()
                );
            }
        }
    }
}
