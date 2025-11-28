//package com.example.demo.entities;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "UsuarioRoles")
//public class UserRoles {
//
//    @Id
//    @Column(name = "IdUsuario")
//    private Long userId; // misma PK que la tabla
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "IdUsuario", nullable = false)
//    private UserEntitie user;
//
//    @Column(name = "AssignedAt", nullable = false)
//    private LocalDateTime assignedAt;
//
//    @Column(name = "Admin", nullable = false)
//    private Boolean admin;
//
//    @Column(name = "User", nullable = false)
//    private Boolean userRole; // 'User' es palabra reservada en Java, usamos userRole
//
//    @Column(name = "Guest", nullable = false)
//    private Boolean guest;
//
//    @Column(name = "Moderator", nullable = false)
//    private Boolean moderator;
//
//    @PrePersist
//    protected void onCreate() {
//        this.assignedAt = LocalDateTime.now();
//    }
//
////     Getters y setters
//
//    public Long getUserId() { return userId; }
//    public void setUserId(Long userId) { this.userId = userId; }
//
//    public UserEntitie getUser() { return user; }
//    public void setUser(UserEntitie user) { this.user = user; }
//
//    public LocalDateTime getAssignedAt() { return assignedAt; }
//    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
//
//    public Boolean getAdmin() { return admin; }
//    public void setAdmin(Boolean admin) { this.admin = admin; }
//
//    public Boolean getUserRole() { return userRole; }
//    public void setUserRole(Boolean userRole) { this.userRole = userRole; }
//
//    public Boolean getGuest() { return guest; }
//    public void setGuest(Boolean guest) { this.guest = guest; }
//
//    public Boolean getModerator() { return moderator; }
//    public void setModerator(Boolean moderator) { this.moderator = moderator; }
//}
package com.example.demo.entities;

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
    private Integer idRol;

    @Column(name = "AssignedAt", columnDefinition = "datetime2", nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "Role", length = 400, columnDefinition = "nchar(400)")
    private String role;

    public UserRolesEntity() {
    }

    public UserRolesEntity(Long idUsuario, Integer idRol, LocalDateTime assignedAt, String role) {
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

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
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


