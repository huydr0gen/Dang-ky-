package com.tlu.dangkyhoc.model;

import java.util.ArrayList;

public class Course {
	private String maHocPhan;
	private String tenHocPhan;
	private int soTin;
	private ArrayList<String> dieuKienTienQuyet;
	private ArrayList<String> monTuongDuong;
	
	public Course(String maHocPhan, String tenHocPhan, int soTin, ArrayList<String> dieuKienTienQuyet,
			ArrayList<String> monTuongDuong) {
		super();
		this.maHocPhan = maHocPhan;
		this.tenHocPhan = tenHocPhan;
		this.soTin = soTin;
		this.dieuKienTienQuyet = dieuKienTienQuyet;
		this.monTuongDuong = monTuongDuong;
	}

	public String getMaHocPhan() {
		return maHocPhan;
	}

	public void setMaHocPhan(String maHocPhan) {
		this.maHocPhan = maHocPhan;
	}

	public String getTenHocPhan() {
		return tenHocPhan;
	}

	public void setTenHocPhan(String tenHocPhan) {
		this.tenHocPhan = tenHocPhan;
	}

	public int getSoTin() {
		return soTin;
	}

	public void setSoTin(int soTin) {
		this.soTin = soTin;
	}

	public ArrayList<String> getDieuKienTienQuyet() {
		return dieuKienTienQuyet;
	}

	public void setDieuKienTienQuyet(ArrayList<String> dieuKienTienQuyet) {
		this.dieuKienTienQuyet = dieuKienTienQuyet;
	}

	public ArrayList<String> getMonTuongDuong() {
		return monTuongDuong;
	}

	public void setMonTuongDuong(ArrayList<String> monTuongDuong) {
		this.monTuongDuong = monTuongDuong;
	}
}
