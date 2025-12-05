package com.example.demo.app.infrastructure.adapters.input.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.app.aplication.port.in.UserUseCasePort;
import com.example.demo.app.domain.model.User;

@Controller
public class WebController {
	@Autowired
	private UserUseCasePort userUseCase;

	// REDIRECT TO LOGIN
	@GetMapping("/")
	public String root() {
		return "redirect:/login";
	}

	// LOGIN PAGE
	@GetMapping("/login")
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "csrfToken", required = false) String csrfToken, Authentication auth) {
		/*
		 * 
		 * >>>>>>>INPUT ADAPTER<<<<<<<< >>>>>>>CALL USE CASE PORT<<<
		 * 
		 */

		ModelAndView model = new ModelAndView(userUseCase.login(error, logout, csrfToken, auth));

		if (error != null) {
			model.addObject("error", "Usuario o contraseña incorrectos!");
		}
		if (logout != null) {
			model.addObject("msg", "Has cerrado sesión correctamente.");
		}

		return model;
	}
	
	@GetMapping("/register")
	public String showRegisterForm() {
		return "register";
	}
	// REGISTER
	@PostMapping("/register")
	public String register(@RequestParam(value = "csrfToken", required = false) String csrfToken,
			@RequestParam(value = "username", required = true) String userName,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "email", required = true)String email) {
		/*
		 * 
		 * 
		 * >>>>>>>>>>INPUT ADAPTER CALL USE CASE PORT<<<<<<<<<<<<<<<
		 * 
		 * 
		 */
		
		User newUser = userUseCase.createUser(userName, email, password, csrfToken);
		
		if(newUser != null) {
			return "redirect:/login";
		}else {
			return null;
		}
		
		
	}

	// PRIVATE PAGE
	@GetMapping("/private/home")
	public String home() {
		return "private/home";
	}

}
