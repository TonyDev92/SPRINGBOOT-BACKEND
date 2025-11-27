//package com.example.demo.repositories;
//
//import com.example.demo.entities.UserRoles;
//import com.example.demo.entities.UserEntitie;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
//
//    // Obtiene todos los roles asociados a un usuario específico
//    List<UserRoles> findByUser(UserEntitie user);
//
//}
package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.UserRoles;

/**
 * Repositorio para acceder a la tabla UsuarioRoles.
 *
 * Provee método para obtener todos los roles asignados a un usuario por su IdUsuario.
 *
 * Nota: si tu tabla tiene PK compuesta, adapta JpaRepository<UserRoles, CompositeIdClass>.
 */
@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {

    /**
     * Obtiene todos los registros de roles para un usuario.
     * Spring Data genera la consulta basada en el nombre del método.
     *
     * @param idUsuario id del usuario (IdUsuario en la tabla)
     * @return lista de UserRoles (vacía si no hay roles)
     */
    List<UserRoles> findByIdUsuario(Long idUsuario);

    // Si prefieres otro nombre o la PK es compuesta, cámbialo según convenga.
}
