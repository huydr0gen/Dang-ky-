package com.tlu.dangkyhoc.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

//import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.tlu.dangkyhoc.model.User;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {
	
	// hàm thử
	public String getExcelUserData() throws IOException {
		try {
			FileInputStream fis = new FileInputStream("D:\\JetBrains\\testApachePOI.xlsx");
			try (Workbook workbook = new XSSFWorkbook(fis)) {
				Sheet sheet = workbook.getSheetAt(2);
				Row row = sheet.getRow(1);
				Cell userCell = row.getCell(0);
				Cell passCell = row.getCell(1);
				return userCell.toString() + " - " + passCell.toString();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public void addNewUser(User user) throws IOException {
		// TODO Auto-generated method stub
		//lay username va password xong ghi vao file excel
			FileInputStream fis = new FileInputStream("D:\\JetBrains\\testApachePOI.xlsx");
			try (Workbook workbook = new XSSFWorkbook(fis)) {
				Sheet sheet = workbook.getSheetAt(2);
				
				int lastRowCount = sheet.getLastRowNum();
				Row row = sheet.createRow(++lastRowCount);
				row.createCell(0).setCellValue(user.getUsername());
				row.createCell(1).setCellValue(user.getPassword());				
				
				try (FileOutputStream fos = new FileOutputStream("D:\\JetBrains\\testApachePOI.xlsx")) {
					workbook.write(fos);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			
			//return user;
	}

	public void authenticateUser(User user, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		// lấy username và password từ form nhập xong so sánh với data trong file excel, 
		// đúng thì chuyển route nhá, sai thì hiện thông báo chẳng hạn?
		// sau cái file input stream đổi thành cho người ta chọn file nhé đừng để mặc định đường link như này
		String username = user.getUsername();
		String password = user.getPassword();
		
		boolean foundUser = false;
		boolean passwordMatchesUser = false;
		
		FileInputStream fis = new FileInputStream("D:\\JetBrains\\testApachePOI.xlsx");
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(2);
		
		for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
			Row row = sheet.getRow(r);
			Cell userCell = row.getCell(0);
			
			if (userCell.getStringCellValue().equals(username)) {
				foundUser = true;
				Cell passwordCell = row.getCell(1);
				
				if (passwordCell.getStringCellValue().equals(password)) {
					passwordMatchesUser = true;
					break;
				}
			}
		}
		
		if (foundUser && passwordMatchesUser) {
			//JOptionPane.showMessageDialog(null, "logged in");
			response.sendRedirect("/home");
		} else {
			//JOptionPane.showMessageDialog(null, "invalid input");
		}
		FileOutputStream fos = new FileOutputStream("D:\\JetBrains\\testApachePOI.xlsx");
		workbook.write(fos);
		workbook.close();
	}

	public ArrayList<User> showAll() throws IOException {
		ArrayList<User> users = new ArrayList<User>();
		
		FileInputStream fis = new FileInputStream("D:\\JetBrains\\testApachePOI.xlsx");
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(2); // sheet user
		
		for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
			Row row = sheet.getRow(r);
			
			String username = row.getCell(0).getStringCellValue();
			String password = row.getCell(1).getStringCellValue();
			
			users.add(new User(username, password));
		}
		
		workbook.close();
		fis.close();
		return users;
	}

	public ArrayList<String> listOfEquivalentCourses(String maMon) throws IOException {
		
		ArrayList<String> courses = new ArrayList<String>();
		courses.add(maMon);
		
		FileInputStream fis = new FileInputStream("D:\\JetBrains\\testApachePOI.xlsx");
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(0); // sheet chuong trinh dao tao
		// mặc định để tương đương của môn đầu tiên
		// môn tương đương chỉ cần dùng split ký tự 
		
		for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
			Row row = sheet.getRow(r);
			String courseName = row.getCell(1).getStringCellValue();
			
			if (courseName.equals(maMon)) {
				String sameCourse = row.getCell(5).getStringCellValue();
				String[] courseSplited = sameCourse.split("/");
				for (String part : courseSplited) {
					courses.add(part);
				}
			}
		}
		
		workbook.close();
		fis.close();
		
		return courses;
	}

	public boolean checkForFinalGrade(String msv, String maMon) throws IOException {
		
		boolean isPass = false;
		
		FileInputStream fis = new FileInputStream("D:\\JetBrains\\testApachePOI.xlsx");
		try (Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheetAt(1); // sheet bảng điểm
			
			for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
				Row row = sheet.getRow(r);
				String cellMSV = row.getCell(0).getStringCellValue();
				
				if (cellMSV.equals(msv)) {
					Cell cellMaMon = row.getCell(2);
					if (cellMaMon.getStringCellValue().equals(maMon)) {
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
	
	//kiểm tra số tín

	// phân loại điều kiện tiên quyết
	public void processPrerequisiteCourses (Row row, ArrayList<String> requiredEnroll, ArrayList<String> optionalEnroll) {
		String cellPrerequisiteCourses = row.getCell(4).getStringCellValue();
		if (cellPrerequisiteCourses != null) {
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
	}
	
	//kiểm tra điều kiện tiên quyết môn
	public ArrayList<ArrayList<String>> checkForPrerequisiteCourses(String maMon) throws IOException {
		ArrayList<String> requiredEnroll = new ArrayList<>();
		ArrayList<String> optionalEnroll = new ArrayList<>();
		
		FileInputStream fis = new FileInputStream("D:\\JetBrains\\testApachePOI.xlsx");
		try (Workbook workbook = new XSSFWorkbook(fis)) {
			Sheet sheet = workbook.getSheetAt(0); // sheet chương trình đào tạo
			
			for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
				Row row = sheet.getRow(r);
				String cellCourseName = row.getCell(1).getStringCellValue();
				
				if (cellCourseName.equals(maMon)) {
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
		
		ArrayList<ArrayList<String>> prerequisiteCourses = new ArrayList<ArrayList<String>>();
		prerequisiteCourses.add(requiredEnroll);
		prerequisiteCourses.add(optionalEnroll);
		return prerequisiteCourses;
	}
	
	// flag
	// kiểm tra điểm của các môn trong điều kiện tiên quyết
	// true = được đăng ký môn này
	// false = không được đăng ký
	public boolean checkForCoursePassed(String msv, String maMon) throws IOException {
		ArrayList<ArrayList<String>> dieuKienTienQuyet = checkForPrerequisiteCourses(maMon);
		ArrayList<String> requiredCourse = dieuKienTienQuyet.get(0);
		ArrayList<String> optionalCourse = dieuKienTienQuyet.get(1);
		
		if (requiredCourse.isEmpty() && optionalCourse.isEmpty()) {
			return true;
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
		
		if (duocDangKy(msv, requiredCourse.get(1)) == false) {
			return false;
		}
		
		if (duocDangKy(msv, requiredCourse.get(1)) == true && duocDangKy(msv, requiredCourse.get(0)) == false) {
			for (String course : optionalCourse) {
				if (duocDangKy(msv, course) == true) {
					return true;
				} else {
					return false;
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
	public boolean duocDangKy(String msv, String maMon) throws IOException {
		ArrayList<String> monTuongDuong = listOfEquivalentCourses(maMon);
		monTuongDuong.add(maMon);
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

	public String confirmMessSend(String tn, String num) {
		return sendMessage(tn, num);
	}

	public String sendMessage(String tinNhan, String number) {
		if ("hello".equals(tinNhan)) {
			ArrayList<String> thongDiep = new ArrayList<String>();
			return "Đã nhận được thông điệp: " + tinNhan + " và số: " + number + " và số lượng: " + thongDiep.size();
		} else {
			String[] xinchao = tinNhan.split(" ");
			return xinchao[0];
		}
	}
}

