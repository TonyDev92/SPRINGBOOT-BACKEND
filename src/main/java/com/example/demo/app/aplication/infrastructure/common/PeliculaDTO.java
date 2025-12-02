package com.example.demo.app.aplication.infrastructure.common;

import java.time.LocalDate;

public class PeliculaDTO {
    private String titulo;
    private String descripcion;
    private String reparto;
    private byte[] imagen;
    private String nombreImagen;
    private String genero;
    private LocalDate fechaEstreno;
    private Integer duracion;

    // Constructor
    public PeliculaDTO(String titulo, String descripcion, String reparto, byte[] imagen,
                       String nombreImagen, String genero, LocalDate fechaEstreno, Integer duracion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.reparto = reparto;
        this.imagen = imagen;
        this.nombreImagen = nombreImagen;
        this.genero = genero;
        this.fechaEstreno = fechaEstreno;
        this.duracion = duracion;
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getReparto() { return reparto; }
    public byte[] getImagen() { return imagen; }
    public String getNombreImagen() { return nombreImagen; }
    public String getGenero() { return genero; }
    public LocalDate getFechaEstreno() { return fechaEstreno; }
    public Integer getDuracion() { return duracion; }
}
