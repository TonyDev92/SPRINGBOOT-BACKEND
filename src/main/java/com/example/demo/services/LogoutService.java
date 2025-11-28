package com.example.demo.services;

public interface LogoutService {
	public int logout(String session);

	public void logoutByToken(String token);

}
