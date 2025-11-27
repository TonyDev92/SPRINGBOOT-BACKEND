package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entities.UserEntitie;
import com.example.demo.services.LogoutService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    private final LogoutService logoutService;

    public WebController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    // Redirige "/" a "/login"
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // Página de login
    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              @RequestParam(value = "csrfToken", required = false) String csrfToken,
                              Authentication auth) {

        // Solo redirige a /private/home si ya está autenticado
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return new ModelAndView("redirect:/private/home");
        }

        ModelAndView model = new ModelAndView("login"); // carga login.jsp

        if (error != null) {
            model.addObject("error", "Usuario o contraseña incorrectos!");
        }
        if (logout != null) {
            model.addObject("msg", "Has cerrado sesión correctamente.");
        }

        return model;
    }

    // Página privada
    @GetMapping("/private/home")
    public String home() {
        return "private/home"; // carga private/home.jsp
    }

    // ===============================================
    // LOGOUT
    // ===============================================
    @Autowired
    private LogoutService logoutService1;

    @GetMapping("/logout")
    public String logout(HttpSession session, Authentication auth) {
        Long userId = null;
        if (auth != null && auth.isAuthenticated()) {
            userId = ((UserEntitie) auth.getPrincipal()).getId();
        }
        logoutService.logout(session, userId);
        return "redirect:/login?logout=true";
    }
}

