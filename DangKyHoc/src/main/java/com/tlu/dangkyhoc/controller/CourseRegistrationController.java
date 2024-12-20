package com.tlu.dangkyhoc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tlu.dangkyhoc.model.User;
import com.tlu.dangkyhoc.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CourseRegistrationController {
	
	@Autowired
	UserService userservice;
	
	@GetMapping("/home")
	public String helloUser() {
		return "jackpot";
	}
	
	@GetMapping("/home/allusers")
	public ArrayList<User> showAllUser() throws IOException {
		return userservice.showAll();
	}
	
	@PostMapping("/home/course")
	public ArrayList<String> monTuongDuong(@RequestBody Map<String, String> payload) throws IOException {
		String maMon = payload.get("maMon");
		return userservice.listOfEquivalentCourses(maMon);
	}
	
	@PostMapping("/home/result")
	public boolean diemTK(@RequestBody Map<String, Object> payload) throws IOException {
		String msv = (String) payload.get("msv");
		String maMon = (String) payload.get("maMon");
		return userservice.checkForFinalGrade(msv, maMon);
	}
	
	@PostMapping("/home/dangkyhoc")
	public boolean duocDangKy(@RequestBody Map<String, Object> payload) throws IOException {
		String msv = (String) payload.get("msv");
		String maMon = (String) payload.get("maMon");
		return userservice.isCourseGraded(msv, maMon);
	}
	
	@PostMapping("/home/dktq")
	public ArrayList<ArrayList<String>> dieuKienTienQuyet(@RequestBody Map<String, Object> payload) throws IOException {
		String maMon = (String) payload.get("maMon");
		return userservice.checkForPrerequisiteCourses(maMon);
	}
	
	@PostMapping("/home/dieukientienquyet")
	public boolean isDieuKienTienQuyetPassed(@RequestBody Map<String, Object> payload) throws IOException {
		String msv = (String) payload.get("msv");
		String maMon = (String) payload.get("maMon");
		return userservice.checkCompletedPrerequisite(msv, maMon);
	}
	
	@PostMapping("home/register")
	public boolean isRegisterAvailable(@RequestBody Map<String, String> payload) throws IOException {
		String msv = (String) payload.get("msv");
		String maMon = (String) payload.get("maMon");
		
		return userservice.checkRegistrationConditions(msv, maMon);
	}
	
}
