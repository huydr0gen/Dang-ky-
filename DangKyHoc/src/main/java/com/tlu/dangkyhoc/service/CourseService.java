package com.tlu.dangkyhoc.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tlu.dangkyhoc.model.Course;

public class CourseService {

	public String filePath = "data/testApachePOI.xlsx";
	
	public void splitCourseCondition(Row row, ArrayList<String> courses) {
		String sameCourse = row.getCell(5).getStringCellValue();
		String[] courseSplited = sameCourse.split("/");
		for (String part : courseSplited) {
			courses.add(part);
		}
	}

	//môn tương đương
	public ArrayList<String> listOfEquivalentCourses(String maHocPhan) throws IOException {
		
		ArrayList<String> courses = new ArrayList<String>();
		
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
		Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0); // sheet chuong trinh dao tao
			
			for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
				Row row = sheet.getRow(r);
				String courseName = row.getCell(1).getStringCellValue();
				if (courseName.equals(maHocPhan)) {
					if (row.getCell(5) != null) {
						splitCourseCondition(row, courses);
					} else {
						return courses;
					}
				}
				
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return courses;
	}

	public boolean checkForFinalGrade(String msv, String maHocPhan) throws IOException {
		
		boolean isPass = false;
		
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
			Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(1); // sheet bảng điểm
			
			for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
				Row row = sheet.getRow(r);
				String cellMSV = row.getCell(0).getStringCellValue();
				
				if (cellMSV.equals(msv)) {
					String cellMaMon = row.getCell(2).getStringCellValue();
					if (cellMaMon.equals(maHocPhan)) {
						Cell cellDiemTK = row.getCell(3);
						if (cellDiemTK.getNumericCellValue() >= 0.0) {
							isPass = true;
						}
					}
				}
			}
		}
		
		if (isPass) {
			return true;
		} else {
			return false;
		}
	}
	
	//hàm lấy số tín từ excel
	public int getStudentCreditsFromExcel(String msv) throws IOException {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
			Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(2);
			
			
			for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
				Row row = sheet.getRow(r);
				String cellMSV = row.getCell(1).getStringCellValue();
				
				if (cellMSV.equals(msv)) {
					return (int) row.getCell(3).getNumericCellValue();
				} 
			}
		}
		
		return 0;
	}
	
	//kiểm tra số tín
	public boolean extractCredits(String dktq, int tinchi) {
		String regex = "(>?|>=?)\\s*(\\d+)\\s*tín\\s*chỉ";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(dktq.trim());
		
		if (matcher.find()) {
			String operator = matcher.group(1).trim();
			int credits = Integer.parseInt(matcher.group(2));
			
			switch (operator) {
				case ">":
					return tinchi > credits;
				case ">=":
					return tinchi >= credits;
				default:
					return tinchi >= credits;
			}
		}
		return false;
	}
	

	// phân loại điều kiện tiên quyết
	public void processPrerequisiteCourses (Row row, ArrayList<String> requiredEnroll, ArrayList<String> optionalEnroll) {
		String cellPrerequisiteCourses;
		
		if (row.getCell(4) != null) {
			cellPrerequisiteCourses = row.getCell(4).getStringCellValue();
		} else {
			return;
		}
		
		if (cellPrerequisiteCourses.contains("/")) {
			String[] optional = cellPrerequisiteCourses.split("/");
			
			for (String course : optional) {
				optionalEnroll.add(course);
			}
		} else {
			String[] must = cellPrerequisiteCourses.split(",");
		
			for (String course : must) {
				requiredEnroll.add(course);
			}
		}
	}
	
	//kiểm tra điều kiện tiên quyết môn
	public ArrayList<ArrayList<String>> checkForPrerequisiteCourses(String maHocPhan) throws IOException {
		ArrayList<String> requiredEnroll = new ArrayList<>();
		ArrayList<String> optionalEnroll = new ArrayList<>();
		
		ArrayList<ArrayList<String>> prerequisiteCourses = new ArrayList<ArrayList<String>>();
		
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
			Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0); // sheet chương trình đào tạo
			
			for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
				Row row = sheet.getRow(r);
				String cellCourseName = row.getCell(1).getStringCellValue();
				
				if (cellCourseName.equals(maHocPhan)) {
					processPrerequisiteCourses(row, requiredEnroll, optionalEnroll);
				}
			}
		}
		
		if (requiredEnroll.isEmpty() && !optionalEnroll.isEmpty()) {
			ArrayList<String> newOptionalEnroll = new ArrayList<>();
			for (String course : optionalEnroll) {
				if (course.contains(",")) {
					String[] option = course.split(",");
					for (String optionCourse : option) {
						requiredEnroll.add(optionCourse);
					}
				} else {
					newOptionalEnroll.add(course);
				}
			}
			optionalEnroll = newOptionalEnroll;
		} else {
			ArrayList<String> newRequiredEnroll = new ArrayList<>();
			for (String course : requiredEnroll) {
				String[] must = course.split(",");
				for (String mustCourse : must) {
					newRequiredEnroll.add(mustCourse);
				}
			}
			requiredEnroll = newRequiredEnroll;
		}
		
		prerequisiteCourses.add(requiredEnroll);
		prerequisiteCourses.add(optionalEnroll);
		return prerequisiteCourses;
	}
	
	// flag
	// kiểm tra điểm của các môn trong điều kiện tiên quyết
	// true = được đăng ký môn này
	// false = không được đăng ký
	public boolean checkCompletedPrerequisite(String msv, String maHocPhan) throws IOException {
		ArrayList<ArrayList<String>> dieuKienTienQuyet = checkForPrerequisiteCourses(maHocPhan);
		ArrayList<String> requiredCourse = dieuKienTienQuyet.get(0);
		ArrayList<String> optionalCourse = dieuKienTienQuyet.get(1);
		
		
		if (requiredCourse.isEmpty() && optionalCourse.isEmpty()) {
			return true; // không có điều kiện tiên quyết thì được đăng ký
		}
			
		if (requiredCourse.isEmpty()) {
			for (String course : optionalCourse) {
				if (checkForFinalGrade(msv, course) == true) {
					return true;
				}
			}
		}
		
		if (optionalCourse.isEmpty()) {
			for (String course : requiredCourse) {
				if (checkForFinalGrade(msv, course) == false) {
					return false;
				}
			}
		}
		
		if (isCourseGraded(msv, requiredCourse.get(1)) == false) {
			return false;
		}
		
		if (isCourseGraded(msv, requiredCourse.get(1)) == true) {
			if (requiredCourse.get(0).contains("tín chỉ")) {
				if (extractCredits(requiredCourse.get(0), getStudentCreditsFromExcel(msv)) == true) {
					return true;
				} else {
					return false;
				}
			} else {
				for (String course : optionalCourse) {
					if (isCourseGraded(msv, course) == true) {
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return true;
		}
		
		return false;
	}
	
	// tạo một hàm sử dụng được 2 hàm môn tương đương và điểm tổng kết
	// true là đã có điểm
	// false là chưa có điểm
	public boolean isCourseGraded(String msv, String maHocPhan) throws IOException {
		ArrayList<String> monTuongDuong = listOfEquivalentCourses(maHocPhan);
		monTuongDuong.add(maHocPhan);
		boolean hasGraded = false;
		
		for (String course : monTuongDuong) {
			hasGraded = checkForFinalGrade(msv, course);
			if (hasGraded) {
				break;
			}
		}

		if (hasGraded) {
			return true;
		} else {
			return false;
		}
	}
	
	// tổng hợp 4 điều kiện
	public boolean checkRegistrationConditions(String msv, String maHocPhan) throws IOException {
	    boolean isGraded = isCourseGraded(msv, maHocPhan);
	    boolean isPrerequisiteCompleted = checkCompletedPrerequisite(msv, maHocPhan);

	    System.out.println("isCourseGraded: " + isGraded);  // In giá trị của isCourseGraded
	    System.out.println("checkCompletedPrerequisite: " + isPrerequisiteCompleted);  // In giá trị của checkCompletedPrerequisite

	    return (!isGraded && isPrerequisiteCompleted);
	}
	
	public ArrayList<Course> countStudentsRegister() throws IOException {
		ArrayList<Course> courseList = new ArrayList<>();
		int coTheDangKy = 0;
		
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
			Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheetCourses = workbook.getSheetAt(0);
			Sheet sheetStudents = workbook.getSheetAt(2);
			
			for (int r = 1; r < sheetCourses.getPhysicalNumberOfRows(); r++) {
				Row rowCourseName = sheetCourses.getRow(r);
				String cellCourseId = rowCourseName.getCell(1).getStringCellValue();
				
				for (int s = 1; s < sheetStudents.getPhysicalNumberOfRows(); s++) {
					Row rowStudentsId = sheetStudents.getRow(s);
					String cellStudentId = rowStudentsId.getCell(1).getStringCellValue();
					
					if (checkRegistrationConditions(cellCourseId, cellStudentId)) {
						coTheDangKy++;
					}
					
				}
				
				courseList.add(new Course(cellCourseId, coTheDangKy));
				coTheDangKy = 0;
			}
		}
		
		return courseList;
	}

}
