package com.tlu.dangkyhoc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tlu.dangkyhoc.model.User;
import com.tlu.dangkyhoc.service.UserService;

@RestController
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
	
	// controller này sẽ được gộp với một phương thức tổng, phần mapping sẽ được chỉnh lại cho đúng sau
	@PostMapping("/home/course")
	public ArrayList<String> monTuongDuong(@RequestBody Map<String, String> payload) throws IOException {
		String maMon = payload.get("maMon");
		return userservice.listOfEquivalentCourses(maMon);
	}
	
	// controller này sẽ được gộp với một phương thức tổng, phần mapping sẽ được chỉnh lại cho đúng sau
	// trả về kết quả dạng thông báo "được" hoặc "không"
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
		return userservice.duocDangKy(msv, maMon);
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
		return userservice.checkForCoursePassed(msv, maMon);
	}
	
	// test chức năng mới
	@PostMapping("/home/api/message")
	public String message(@RequestBody Map<String, Object> payload) {
		String tinNhan = (String) payload.get("message");
		String number = (String) payload.get("number");
		return userservice.confirmMessSend(tinNhan, number);
	}
}
