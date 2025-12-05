package com.example.demo.app.infrastructure.adapters.output.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la tabla SQL Server "UsuarioRoles".
 *
 * Notas:
 * - La columna Role en la BD es de tipo nchar (longitud 400) — puede contener padding spaces.
 *   Por eso al leerla aplicamos trim() en el getter/constructor / mapping donde proceda.
 */
@Entity
@Table(name = "UsuarioRoles")
public class UserRolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // No existe una PK compuesta definida en la descripción original: si la tabla tiene PK compuesta (IdUsuario+IdRol)
    // habría que adaptarlo. Aquí asumimos que no hay clave autogenerada y evitamos usar @GeneratedValue si la tabla tiene PK compuesta.
    // Si tu tabla NO tiene columna Id (PK autonum) entonces sustituir este @Id por @EmbeddedId acorde.
    @Column(name = "IdUsuario", nullable = false)
    private Long idUsuario;

    @Column(name = "IdRol", nullable = false)
    private Long idRol;

    @Column(name = "AssignedAt", columnDefinition = "datetime2", nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "Role", length = 400, columnDefinition = "nchar(400)")
    private String role;

    public UserRolesEntity() {
    }

    public UserRolesEntity(Long idUsuario, Long idRol, LocalDateTime assignedAt, String role) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.assignedAt = assignedAt;
        this.role = role;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    /**
     * Obtener role haciendo trim() para eliminar padding por ser nchar en SQL Server.
     */
    public String getRole() {
        return role == null ? null : role.trim();
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserRoles{" +
                "idUsuario=" + idUsuario +
                ", idRol=" + idRol +
                ", assignedAt=" + assignedAt +
                ", role='" + getRole() + '\'' +
                '}';
    }
}


