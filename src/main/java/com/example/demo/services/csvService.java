package com.example.demo.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.dto.PeliculaDTO;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

@Service
public class csvService {

    public List<UsuarioDTO> leerUsuarios(Resource usuariosCsv) throws Exception {
        List<UsuarioDTO> usuarios = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(usuariosCsv.getInputStream(), StandardCharsets.UTF_8))) {

            CSVReader reader = new CSVReaderBuilder(br).withSkipLines(1).build(); // Saltar header
            String[] data;

            while ((data = reader.readNext()) != null) {
                if (data.length < 4) continue; // seguridad

                String username = data[0].trim();
                String email = data[1].trim();
                String password = data[2].trim();
                Integer status = Integer.parseInt(data[3].trim());

                usuarios.add(new UsuarioDTO(username, email, password, status));
            }
        }

        return usuarios;
    }

    public List<PeliculaDTO> leerPeliculas(Resource peliculasCsv) throws Exception {
        List<PeliculaDTO> peliculas = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(peliculasCsv.getInputStream(), StandardCharsets.UTF_8))) {

            CSVReader reader = new CSVReaderBuilder(br).withSkipLines(1).build(); // Saltar header
            String[] data;

            while ((data = reader.readNext()) != null) {
                if (data.length < 8) continue; // seguridad

                String titulo = data[0].trim();
                String descripcion = data[1].trim();
                String reparto = data[2].trim();
                String imagenBase64 = data[3].trim();
                String nombreImagen = data[4].trim();
                String genero = data[5].trim();
                LocalDate fechaEstreno = LocalDate.parse(data[6].trim(), formatter);
                Integer duracion = Integer.parseInt(data[7].trim());

                byte[] imagenBytes = imagenBase64.isEmpty() ? null : Base64.getDecoder().decode(imagenBase64);

                peliculas.add(new PeliculaDTO(titulo, descripcion, reparto, imagenBytes,
                        nombreImagen, genero, fechaEstreno, duracion));
            }
        }

        return peliculas;
    }
}
