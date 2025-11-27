package com.example.demo.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.entities.UserEntitie;
import com.example.demo.entities.UserRoles;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserRolesRepository;

import jakarta.servlet.http.HttpSession;

/**
 * Custom AuthenticationProvider que:
 * - Valida credenciales contra la tabla de usuarios (usando BCrypt).
 * - Carga roles desde la tabla UsuarioRoles y los convierte en GrantedAuthority.
 *
 * Comentarios críticos:
 * - He corregido la validación de passwords: ahora se usa passwordEncoder.matches(raw, storedHash).
 * - Si tu UserEntitie tiene getters con otros nombres, adapta las llamadas (getPasswordHash(), getId(), getStatus()).
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private String name;
    private String password;
    private String userToken;
    private List<GrantedAuthority> authorities;

    private final UserRepository repository;
    private final UserRolesRepository repositoryRole;
    private UserEntitie userResponse;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserRepository repository,
                                        UserRolesRepository repositoryRole,
                                        BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.repositoryRole = repositoryRole;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        clearCredentials();

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);

        // 1) Intentar login (verifica existencia y contraseña)
        if (login(authentication) && isRequestHeaderRefererAllowed(attr)) {
            // Generar token de sesión
            this.userToken = UUID.randomUUID().toString();
            session.setAttribute("userToken", userToken);

            this.name = authentication.getName();
            this.password = authentication.getCredentials().toString();

            // 2) Cargar roles desde UsuarioRoles
            authorities = new ArrayList<>();
            try {
                if (userResponse != null && userResponse.getId() != null) {
                    List<UserRoles> roles = repositoryRole.findByIdUsuario(userResponse.getId());

                    if (roles != null) {
                        for (UserRoles ur : roles) {
                            String roleRaw = ur.getRole();
                            if (roleRaw == null || roleRaw.isBlank()) {
                                continue;
                            }
                            // Normalizar: eliminar whitespace y asegurar prefijo ROLE_
                            String normalized = roleRaw.trim();
                            if (!normalized.startsWith("ROLE_")) {
                                normalized = "ROLE_" + normalized;
                            }
                            authorities.add(new SimpleGrantedAuthority(normalized));
                        }
                    }
                }
            } catch (Exception ex) {
                // No propagamos excepción de lectura de roles aquí; registrarlo es recomendable (logger)
                // Por simplicidad, si hay fallo solo dejamos que continúe sin roles cargados.
                // logger.error("Error cargando roles de UsuarioRoles", ex);
            }

            // 3) Si no hay roles asignados, añadir un ROLE_USER por defecto
//            if (authorities.isEmpty()) {
//                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//            }

            // Devolver token de autenticación validado con las autoridades cargadas
            System.out.println("===== ROLES DEL USUARIO AUTENTICADO =====");
            for (GrantedAuthority a : authorities) {
                System.out.println("Rol asignado: " + a.getAuthority());
            }
            System.out.println("==========================================");

            return new UsernamePasswordAuthenticationToken(name, null, authorities);
        } else {
            throw new BadCredentialsException("Credenciales inválidas");
        }
    }

    /**
     * Realiza la comprobación de credenciales:
     * - Busca el usuario por email (authentication.getName()).
     * - Si existe, compara password plano con hash almacenado usando BCrypt.matches.
     * - Comprueba también el status si aplica (por ejemplo, status != 0 -> activo). Ajustar según tu semántica.
     *
     * IMPORTANTE: Asumo que UserEntitie tiene:
     *   - Long getId()
     *   - String getPasswordHash()
     *   - Integer getStatus()
     *
     * Si los nombres son distintos, adapta el código.
     */
    public boolean login(Authentication authentication) {
        String email = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        userResponse = repository.findByEmail(email).orElse(null);

        if (userResponse == null) {
            // usuario no encontrado
            return false;
        }

        // Obtener hash almacenado
        String storedHash = null;
        try {
            storedHash = userResponse.getPasswordHash();
        } catch (Exception e) {
            // Si la entidad no tiene este getter o es nulo, tratamos como no match.
//             logger.warn("UserEntitie no expone passwordHash o es nulo", e);
            return false;
        }

        if (storedHash == null || storedHash.isEmpty()) {
            return false;
        }

        // Verificar password con BCrypt
        boolean matches = passwordEncoder.matches(rawPassword, storedHash);

        if (!matches) {
            return false;
        }

        // Opcional: comprobar status del usuario (por ejemplo: 1 = activo)
        try {
            Byte status = userResponse.getStatus();
            // Ajusta la lógica según cómo uses 'Status' en la tabla (tinyint)
            if (status != null && status == 0) {
                // ejemplo: status == 0 -> inactivo
                return false;
            }
        } catch (Exception ex) {
            // Si no existe getStatus(), se asume activo
        }

        return true;
    }

    /**
     * Limpia credenciales en memoria.
     */
    public void clearCredentials() {
        name = null;
        password = null;
        userToken = null;
        if (authorities == null) {
            authorities = new ArrayList<>();
        }
        authorities.clear();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public String getUserToken() {
        return userToken;
    }

    /**
     * Comprueba el header referer. Durante pruebas devuelve true; hacer validación real en producción.
     */
    public boolean isRequestHeaderRefererAllowed(ServletRequestAttributes attr) {
        return true;
    }
}
/*
 * 
 * 
 * 
 * 
 * VERSION ANTERIOR
 * 
 * 
 * 
*/
//package com.example.demo.auth;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.example.demo.entities.UserEntitie;
//import com.example.demo.repositories.UserRepository;
//import com.example.demo.repositories.UserRolesRepository;
//
///**
//* The Class CustomAuthenticationProvider.
//*/
//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//	/** The name. */
//	private String name;
//
//	/** The password. */
//	private String password;
//
//	/** The user token. */
//	private String userToken;
//
//	/** The authorities. */
//	private List<GrantedAuthority> authorities;
//	private UserRepository repository;
//	private UserRolesRepository repositoryRole;
//	private UserEntitie userResponse;
//	
//  @Autowired
//  private BCryptPasswordEncoder passwordEncoder;
//
//	/**
//	 * Authenticate.
//	 *
//	 * @param authentication the authentication
//	 * @return the authentication
//	 * @throws AuthenticationException the authentication exception
//	 */
//	@Override
//	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//		clearCredentials();
//
//		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//		jakarta.servlet.http.HttpSession session = attr.getRequest().getSession(true);
//		if (login(authentication) && isRequestHeaderRefererAllowed(attr)) {
//			session.setAttribute("userToken", userToken);
//			this.name = authentication.getName();
//			this.password = authentication.getCredentials().toString();
//
//			// Agregar un rol para que Spring Security lo reconozca
//			if (authorities == null) {
//				authorities = new ArrayList<>();
//				
//				userResponse = new UserEntitie();
//				repositoryRole.getById(userResponse.getId());
//				
//				//for(data) {
//					//authorities.add(() -> data.role);
//					
//				//}
//
//				
//			}
//			authorities = new ArrayList<>(); authorities.add(() -> "ROLE_USER");
//
//			return new UsernamePasswordAuthenticationToken(name, password, authorities);
//		} else {
//			throw new BadCredentialsException("Credenciales inválidas");
//		}
//
//		/*
//		 * // Código funcional de pruebas locales ServletRequestAttributes attr =
//		 * (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//		 * jakarta.servlet.http.HttpSession session =
//		 * attr.getRequest().getSession(true); // crea sesión si no existe
//		 * 
//		 * if (login(authentication)) { session.setAttribute("userToken", userToken);
//		 * this.name = authentication.getName(); this.password =
//		 * authentication.getCredentials().toString();
//		 * 
//		 * // Agregar un rol para que Spring Security lo reconozca if (authorities ==
//		 * null) authorities = new ArrayList<>(); authorities.add(() -> "ROLE_USER"); //
//		 * <<<<< Esto es clave
//		 * 
//		 * return new UsernamePasswordAuthenticationToken(name, password, authorities);
//		 * } else { return null; }
//		 */
//
//	}
//
//	/**
//	 * Login.
//	 *
//	 * @param authentication the authentication
//	 * @return true, if successful
//	 */
//	
//		@Autowired
//		public CustomAuthenticationProvider(UserRepository repository) {
//			this.repository = repository;
//		}
//	public boolean login(Authentication authentication) {
//		UserEntitie user = new UserEntitie();
//		
//		user.setEmail(authentication.getName());
//		user.setPasswordHash(passwordEncoder.encode(authentication.getCredentials().toString()));
//
//		userResponse = new UserEntitie();
//		userResponse = repository.findByEmail(user.getEmail()).orElse(null);
//		System.out.println("Password :");
//		
//		System.out.println("User Response:");
//		System.out.print(user);
//		
//		if (userResponse == null) {
//			return false;
//		} else {
//			return true;
//		}
//		// Autenticación siempre exitosa para pruebas
//		// return true;
//		// return "admin".equals(authentication.getName()) &&
//		// "1234".equals(authentication.getCredentials().toString());
//	}
//
//	/**
//	 * Clear credentials.
//	 */
//	public void clearCredentials() {
//		name = null;
//		password = null;
//		userToken = null;
//		if (authorities == null) {
//			authorities = new ArrayList<>();
//		}
//		authorities.clear();
//	}
//
//	/**
//	 * Supports.
//	 *
//	 * @param authentication the authentication
//	 * @return true, if successful
//	 */
//	@Override
//	public boolean supports(Class<?> authentication) {
//		return authentication.equals(UsernamePasswordAuthenticationToken.class);
//	}
//
//	/**
//	 * Gets the user token.
//	 *
//	 * @return the user token
//	 */
//	public String getUserToken() {
//		return userToken;
//	}
//
//	/**
//	 * Checks if is request header referer allowed.
//	 *
//	 * @param attr the attr
//	 * @return true, if is request header referer allowed
//	 */
//	public boolean isRequestHeaderRefererAllowed(ServletRequestAttributes attr) {
//		/*
//		 * boolean refererAllowed = false; String referer =
//		 * attr.getRequest().getHeader("referer"); // Properties y validación return
//		 * refererAllowed;
//		 */
//		return true; // Siempre true durante pruebas
//	}
//}
