package com.tlu.dangkyhoc.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tlu.dangkyhoc.model.LoginResponse;
import com.tlu.dangkyhoc.model.User;
import com.tlu.dangkyhoc.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {
	@Autowired
	UserService userService;
	
	@PostMapping("/login")
	public LoginResponse doLogin(@RequestBody User user, HttpServletResponse response) throws IOException {
		return userService.authenticateUser(user, response);
	}
	
	@PostMapping("/user")
	public User create(@RequestBody User user) throws IOException {
		userService.addNewUser(user);
		return user;
	}
	
}
