package com.tlu.dangkyhoc.model;

import java.util.ArrayList;

public class Course {
	private String maHocPhan;
	private String tenHocPhan;
	int soTinChi;
	ArrayList<ArrayList<String>> tienQuyet;
	ArrayList<String> monTuongDuong;
	int khoaId;
	private int soLuongSinhVienDangKy;
	
	public Course(String maHocPhan, int soLuongSinhVienDangKy) {
		super();
		this.maHocPhan = maHocPhan;
		this.soLuongSinhVienDangKy = soLuongSinhVienDangKy;
	}
	
	public Course(String maHocPhan, String tenHocPhan, int soTinChi, ArrayList<ArrayList<String>> tienQuyet,
			ArrayList<String> monTuongDuong, int khoaId) {
		super();
		this.maHocPhan = maHocPhan;
		this.tenHocPhan = tenHocPhan;
		this.soTinChi = soTinChi;
		this.tienQuyet = tienQuyet;
		this.monTuongDuong = monTuongDuong;
		this.khoaId = khoaId;
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
	public int getSoTinChi() {
		return soTinChi;
	}
	public void setSoTinChi(int soTinChi) {
		this.soTinChi = soTinChi;
	}
	public ArrayList<ArrayList<String>> getTienQuyet() {
		return tienQuyet;
	}
	public void setTienQuyet(ArrayList<ArrayList<String>> tienQuyet) {
		this.tienQuyet = tienQuyet;
	}
	public ArrayList<String> getMonTuongDuong() {
		return monTuongDuong;
	}
	public void setMonTuongDuong(ArrayList<String> monTuongDuong) {
		this.monTuongDuong = monTuongDuong;
	}
	public int getKhoaId() {
		return khoaId;
	}
	public void setKhoaId(int khoaId) {
		this.khoaId = khoaId;
	}
	
}
