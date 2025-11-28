package com.example.demo.config;

import com.example.demo.auth.CustomAuthenticationProvider;
import com.example.demo.security.CustomLogoutSuccessHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthProvider;

    public SecurityConfig(CustomAuthenticationProvider customAuthProvider) {
        this.customAuthProvider = customAuthProvider;
    }
    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    // Custom authentication manager
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authentication -> customAuthProvider.authenticate(authentication);
    }

    // SECURITY
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.authorizeHttpRequests(auth -> auth
                .requestMatchers("/private/**").hasAnyRole("USER", "ADMIN") // USER && ADMIN ONLY
                .requestMatchers("/**").permitAll()
            )
            // LOGIN
            .formLogin(form -> form
                .loginPage("/login")                 
                .loginProcessingUrl("/login")        
                .defaultSuccessUrl("/private/home", true)  
                .failureUrl("/login?error=true")     
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            // LOGOUT
             .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/auth?logout=true")
                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                    )

            .csrf(Customizer.withDefaults());
        
        return http.build();
    }
}
