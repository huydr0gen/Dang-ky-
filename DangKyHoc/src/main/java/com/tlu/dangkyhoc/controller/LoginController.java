package com.tlu.dangkyhoc.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tlu.dangkyhoc.model.User;
import com.tlu.dangkyhoc.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LoginController {
	@Autowired
	UserService userService;
	
	@GetMapping("/login")
	public String excelUserData() throws IOException {
		return userService.getExcelUserData();
	}
	
	@GetMapping("/logins/user")
	public void doLogin(@RequestBody User user, HttpServletResponse response) throws IOException {
		userService.authenticateUser(user, response);
	}
	
	@PostMapping("/user")
	public User create(@RequestBody User user) throws IOException {
		userService.addNewUser(user);
		return user;
	}
	
}
