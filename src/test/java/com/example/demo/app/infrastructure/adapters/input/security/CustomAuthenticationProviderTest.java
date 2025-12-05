package com.example.demo.app.infrastructure.adapters.input.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.app.aplication.service.UserAplicationService;
import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserEntity;
import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserRolesEntity;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.UserRepository;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.UserRolesRepository;
import com.example.demo.app.infrastructure.adapters.output.persistence.repository.UserTokenRepository;

class CustomAuthenticationProviderTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRolesRepository userRolesRepository;

    @Mock
    private UserTokenRepository tokenRepository;

    @Mock
    private UserAplicationService userAplicationService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private CustomAuthenticationProvider authProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializar authProvider con todas las dependencias por constructor
        authProvider = new CustomAuthenticationProvider(
                userRepository,
                userRolesRepository,
                tokenRepository,
                passwordEncoder,
                userAplicationService // ahora inyectado directamente
        );
    }

    // Método de ayuda para mockear RequestContextHolder
    private HttpSession setupRequestContext() {
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(true)).thenReturn(session);

        ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
        when(attributes.getRequest()).thenReturn(request);

        RequestContextHolder.setRequestAttributes(attributes);
        return session;
    }

    private void tearDownRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void testAuthenticateSuccess() {
        HttpSession session = setupRequestContext();

        // 1️ Preparar usuario válido
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setPasswordHash("hashedPassword");
        user.setStatus((byte) 1);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(userAplicationService.checkPassword("password", "hashedPassword")).thenReturn(true);

        // 2️ Mock roles
        UserRolesEntity role = new UserRolesEntity();
        role.setIdUsuario(1L);
        role.setRole("ADMIN");
        when(userRolesRepository.findByIdUsuario(1L)).thenReturn(List.of(role));

        // 3️ Ejecutar authenticate
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken("user@test.com", "password");

        UsernamePasswordAuthenticationToken result =
                (UsernamePasswordAuthenticationToken) authProvider.authenticate(authToken);

        // 4️ Verificar token generado
        assertNotNull(authProvider.getUserToken(), "El token no debe ser nulo");

        // 5️ Verificar saveUserToken y session.setAttribute
        verify(tokenRepository, times(1)).save(any());
        verify(session, times(1)).setAttribute("userToken", authProvider.getUserToken());
        verify(session, times(1)).setAttribute("userId", 1L);

        // 6️ Verificar roles cargados
        assertEquals(1, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        tearDownRequestContext();
    }

    @Test
    void testAuthenticateBadCredentials_UserNotFound() {
        setupRequestContext();

        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken("unknown@test.com", "password");

        assertThrows(BadCredentialsException.class, () -> authProvider.authenticate(authToken));
        verify(tokenRepository, never()).save(any());

        tearDownRequestContext();
    }

    @Test
    void testAuthenticateBadCredentials_WrongPassword() {
        HttpSession session = setupRequestContext();

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setPasswordHash("hashedPassword");
        user.setStatus((byte) 1);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(userAplicationService.checkPassword("wrong", "hashedPassword")).thenReturn(false);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken("user@test.com", "wrong");

        assertThrows(BadCredentialsException.class, () -> authProvider.authenticate(authToken));
        verify(tokenRepository, never()).save(any());

        tearDownRequestContext();
    }

    @Test
    void testAuthenticateBadCredentials_UserInactive() {
        HttpSession session = setupRequestContext();

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setPasswordHash("hashedPassword");
        user.setStatus((byte) 0); // Usuario inactivo

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(userAplicationService.checkPassword("password", "hashedPassword")).thenReturn(true);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken("user@test.com", "password");

        assertThrows(BadCredentialsException.class, () -> authProvider.authenticate(authToken));
        verify(tokenRepository, never()).save(any());

        tearDownRequestContext();
    }

    @Test
    void testSupports() {
        assertTrue(authProvider.supports(UsernamePasswordAuthenticationToken.class));
        assertFalse(authProvider.supports(String.class));
    }
}


