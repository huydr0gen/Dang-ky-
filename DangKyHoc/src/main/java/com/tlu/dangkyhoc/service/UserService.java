package com.tlu.dangkyhoc.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tlu.dangkyhoc.model.Course;
import com.tlu.dangkyhoc.model.User;
import com.tlu.dangkyhoc.repository.UserRepository;



@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public String filePath = "data/testApachePOI.xlsx";
	
	public ResponseEntity<String> doLogin(String username, String password) {
		Optional<User> userOpt = userRepository.findByUsername(username);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (user.getPassword().equals(password)) {
				return ResponseEntity.ok("Login successful!");
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
	}
	
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public User saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(String toEmail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}
	
	public void sendResetToken(String email) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		
		User user = userOpt.get();
		
		String token = UUID.randomUUID().toString();
		user.setResetToken(token);
		userRepository.save(user);
		
		String resetLink = "http://localhost:8080/login/reset-password?token=" + token;
		sendEmail(
				user.getEmail(), 
				"Reset mật khẩu", 
				"Nhấp vào liên kết sau để đặt lại mật khẩu" + resetLink);
	}
	
	public void resetPassword(String token, String newPassword) {
		Optional<User> userOpt = userRepository.findByResetToken(token);
		
		if (userOpt.isEmpty()) {
			throw new IllegalArgumentException("Token không hợp lệ!");
		}
		
		User user = userOpt.get();
		
		String encodePassword = new BCryptPasswordEncoder().encode(newPassword);
		user.setPassword(encodePassword);
		
		user.setResetToken(null);
		userRepository.save(user);
	}

	public User addUser(User user) {	
			return userRepository.save(user);
	}
	
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	public List<User> showAllUser() {
		return userRepository.findAll();
	}
	
	public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
	
	public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}
