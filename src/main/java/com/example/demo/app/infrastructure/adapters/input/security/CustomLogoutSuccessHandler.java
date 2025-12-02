package com.example.demo.app.infrastructure.adapters.input.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.services.LogoutService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private LogoutService logoutService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {

        if(authentication != null) {
            String sessionId = ((WebAuthenticationDetails) authentication.getDetails()).getSessionId();
            int deleted = logoutService.logout(sessionId);
            System.out.println(">>> TOKENS BORRADOS: " + deleted);
        }

        response.sendRedirect("/login?logout=true");
    }

}
