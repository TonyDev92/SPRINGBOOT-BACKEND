package com.example.demo.controller;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class WebController {
	
	// REDIRECT TO LOGIN
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // LOGIN PAGE
    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout,
                              @RequestParam(value = "csrfToken", required = false) String csrfToken,
                              Authentication auth) {

        // ONLY REDIRECT IF USER IS AUTHENTICATED
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return new ModelAndView("redirect:/private/home");
        }

        ModelAndView model = new ModelAndView("login"); 

        if (error != null) {
            model.addObject("error", "Usuario o contraseña incorrectos!");
        }
        if (logout != null) {
            model.addObject("msg", "Has cerrado sesión correctamente.");
        }

        return model;
    }

    // PRIVATE PAGE
    @GetMapping("/private/home")
    public String home() {
        return "private/home"; 
    }


}

