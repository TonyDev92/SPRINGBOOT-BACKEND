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
import com.example.demo.entities.UserToken;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserRolesRepository;
import com.example.demo.repositories.UserTokenRepository;

import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private String name;
    private String password;
    private String userToken;
    private List<GrantedAuthority> authorities;

    private final UserRepository repository;
    private final UserRolesRepository repositoryRole;
    private final UserTokenRepository tokenRepository;

    private UserEntitie userResponse;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(UserRepository repository,
                                        UserRolesRepository repositoryRole,
                                        UserTokenRepository tokenRepository,
                                        BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.repositoryRole = repositoryRole;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        clearCredentials();

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);

        if (login(authentication) && isRequestHeaderRefererAllowed(attr)) {

            // 1) Generar token
            this.userToken = UUID.randomUUID().toString();
            session.setAttribute("userToken", userToken);

            // 2) Guardar token en la tabla UserTokens asociado a SessionId
            saveUserToken(session);

            this.name = authentication.getName();
            this.password = authentication.getCredentials().toString();

            // 3) Cargar roles del usuario
            authorities = new ArrayList<>();
            try {
                if (userResponse != null && userResponse.getId() != null) {
                    List<UserRoles> roles = repositoryRole.findByIdUsuario(userResponse.getId());
                    if (roles != null) {
                        for (UserRoles ur : roles) {
                            String roleRaw = ur.getRole();
                            if (roleRaw == null || roleRaw.isBlank()) continue;

                            String normalized = roleRaw.trim();
                            if (!normalized.startsWith("ROLE_")) {
                                normalized = "ROLE_" + normalized;
                            }
                            authorities.add(new SimpleGrantedAuthority(normalized));
                        }
                    }
                }
            } catch (Exception ex) {
                // No interrumpimos login por fallo de roles
            }

            return new UsernamePasswordAuthenticationToken(name, null, authorities);
        } else {
            throw new BadCredentialsException("Credenciales inválidas");
        }
    }

    /**
     * Guarda el token generado en la tabla UserTokens, asociado a la sesión.
     */
    private void saveUserToken(HttpSession session) {
        if (userResponse == null || userResponse.getId() == null) return;

        UserToken token = new UserToken();
        token.setIdUsuario(userResponse.getId());
        token.setToken(this.userToken);
        token.setIsActive(true);
        token.setSessionId(session.getId()); // Asociar token a sesión
        tokenRepository.save(token);
    }

    public boolean login(Authentication authentication) {
        String email = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        userResponse = repository.findByEmail(email).orElse(null);
        if (userResponse == null) return false;

        String storedHash = userResponse.getPasswordHash();
        if (storedHash == null || storedHash.isEmpty()) return false;

        boolean matches = passwordEncoder.matches(rawPassword, storedHash);
        if (!matches) return false;

        try {
            Byte status = userResponse.getStatus();
            if (status != null && status == 0) return false;
        } catch (Exception ex) {}

        return true;
    }

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
     * Validación de headers (por simplicidad siempre true, se puede ampliar)
     */
    public boolean isRequestHeaderRefererAllowed(ServletRequestAttributes attr) {
        return true;
    }
}

