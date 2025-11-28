package com.example.demo.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import com.example.demo.entities.UserEntity;
import com.example.demo.entities.UserRolesEntity;
import com.example.demo.entities.UserTokenEntity;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserRolesRepository;
import com.example.demo.repositories.UserTokenRepository;

import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private String name;
    private String userToken;
    private List<GrantedAuthority> authorities;

    private final UserRepository repository;
    private final UserRolesRepository repositoryRole;
    private final UserTokenRepository tokenRepository;

    private UserEntity userResponse;

    private final BCryptPasswordEncoder passwordEncoder;

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

            // 1) GENERATE TOKEN
            this.userToken = UUID.randomUUID().toString();
            session.setAttribute("userToken", userToken);

            // 2) SAVE TOKEN ON USER TOKEN TABLE
            saveUserToken(session);

            this.name = authentication.getName();
            authentication.getCredentials().toString();

            // 3) LOAD USER ROLES
            authorities = new ArrayList<>();
            try {
                if (userResponse != null && userResponse.getId() != null) {
                    List<UserRolesEntity> roles = repositoryRole.findByIdUsuario(userResponse.getId());
                    if (roles != null) {
                        for (UserRolesEntity ur : roles) {
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
            	System.out.println("Exception : " + ex.getMessage());
            }

            return new UsernamePasswordAuthenticationToken(name, null, authorities);
        } else {
            throw new BadCredentialsException("Credenciales inválidas");
        }
    }

    /**
     * SAVE THE TOKEN ON USER TOKENS
     */
    private void saveUserToken(HttpSession session) {
        if (userResponse == null || userResponse.getId() == null) return;

        UserTokenEntity token = new UserTokenEntity();
        token.setIdUsuario(userResponse.getId());
        token.setToken(this.userToken);
        token.setIsActive(true);
        token.setSessionId(session.getId()); // ASOCIATE TOKEN TO SESSION BY SESSION ID
        session.setAttribute("userId", userResponse.getId());

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
     * HEADERS VALIDATION
     */
    public boolean isRequestHeaderRefererAllowed(ServletRequestAttributes attr) {
        return true;
    }
}

