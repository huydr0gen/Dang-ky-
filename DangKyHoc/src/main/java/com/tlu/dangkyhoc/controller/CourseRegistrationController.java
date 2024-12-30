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

import com.tlu.dangkyhoc.model.Course;
import com.tlu.dangkyhoc.service.CourseService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CourseRegistrationController {
	
	@Autowired
	CourseService courseService;
	
	@GetMapping("/home")
	public String helloUser() {
		return "jackpot";
	}
	
	@PostMapping("/home/course")
	public ArrayList<String> monTuongDuong(@RequestBody Map<String, Object> payload) throws IOException {
		String maHocPhan = (String) payload.get("maHocPhan");
		return courseService.listOfEquivalentCourses(maHocPhan);
	}
	
	@PostMapping("/home/result")
	public boolean diemTK(@RequestBody Map<String, Object> payload) throws IOException {
		String msv = (String) payload.get("msv");
		String maHocPhan = (String) payload.get("maHocPhan");
		return courseService.checkForFinalGrade(msv, maHocPhan);
	}
	
	@PostMapping("/home/dangkyhoc")
	public boolean duocDangKy(@RequestBody Map<String, Object> payload) throws IOException {
		String msv = (String) payload.get("msv");
		String maHocPhan = (String) payload.get("maHocPhan");
		return courseService.isCourseGraded(msv, maHocPhan);
	}
	
	@PostMapping("/home/dktq")
	public ArrayList<ArrayList<String>> dieuKienTienQuyet(@RequestBody Map<String, Object> payload) throws IOException {
		String maHocPhan = (String) payload.get("maHocPhan");
		return courseService.checkForPrerequisiteCourses(maHocPhan);
	}
	
	@PostMapping("/home/dieukientienquyet")
	public boolean isDieuKienTienQuyetPassed(@RequestBody Map<String, Object> payload) throws IOException {
		String msv = (String) payload.get("msv");
		String maHocPhan = (String) payload.get("maHocPhan");
		return courseService.checkCompletedPrerequisite(msv, maHocPhan);
	}
	
	@PostMapping("home/register")
	public boolean isRegisterAvailable(@RequestBody Map<String, String> payload) throws IOException {
		String msv = (String) payload.get("msv");
		String maHocPhan = (String) payload.get("maHocPhan");
		
		return courseService.checkRegistrationConditions(msv, maHocPhan);
	}
	
	@PostMapping("/home/register/list")
	public ArrayList<Course> listCourseRegister() throws IOException {
		return courseService.countStudentsRegister();
	}
	
}
