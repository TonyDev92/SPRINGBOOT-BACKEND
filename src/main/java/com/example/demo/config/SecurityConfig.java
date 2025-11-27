package com.example.demo.config;

import com.example.demo.auth.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthProvider;

    public SecurityConfig(CustomAuthenticationProvider customAuthProvider) {
        this.customAuthProvider = customAuthProvider;
    }
   
    // AuthenticationManager personalizado
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authentication -> customAuthProvider.authenticate(authentication);
    }

    // Configuración de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.authorizeHttpRequests(auth -> auth
                .requestMatchers("/private/**").hasAnyRole("USER", "ADMIN") // SOLO USER O ADMIN
                .requestMatchers("/**").permitAll()
            )
            // Configuración del login
            .formLogin(form -> form
                .loginPage("/login")                 // GET /login muestra el formulario
                .loginProcessingUrl("/login")        // POST /login procesa login
                .defaultSuccessUrl("/private/home", true)    // redirige a /home tras login exitoso
                .failureUrl("/login?error=true")     // redirige a /login con error
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            // Configuración del logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // CSRF por defecto
            .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/logout") // ignora CSRF solo para logout
                )
            .csrf(Customizer.withDefaults());
        
    	/*http
        .authorizeHttpRequests(auth -> auth.requestMatchers("/private/**").hasRole("USER"))
        .authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll())
        .formLogin(form -> form
        		.loginPage("/login")
        		.defaultSuccessUrl("/private/home", true)
        		.permitAll())
        .csrf(Customizer.withDefaults());*/

        return http.build();
    }
}
