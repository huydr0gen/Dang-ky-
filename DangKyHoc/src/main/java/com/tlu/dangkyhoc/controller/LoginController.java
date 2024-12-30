package com.tlu.dangkyhoc.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tlu.dangkyhoc.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {
	@Autowired
	UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map <String, String> payload) {
		String username = payload.get("username");
		String password = payload.get("password");
		return userService.doLogin(username, password);
	}
	
	@PostMapping("/login/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		try {
			userService.sendResetToken(email);
			return ResponseEntity.ok("Reset token đã được gửi đến email của bạn");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PostMapping("login/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
		String token = request.get("token");
		String newPassword = request.get("newPassword");
		userService.resetPassword(token, newPassword);
		return ResponseEntity.ok("Mật khẩu đã được reset thành công");
	}
}
