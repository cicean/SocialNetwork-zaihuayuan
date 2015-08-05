package com.gzqining.inthegarden.vo;

import java.util.List;

public class PhotoAlbumBack {
	private String message;
	private String code;
	private String count;
	private int totalPages;
	private String totalRows;
	private int nowPage;
	private List<PhotoAlbumBackList> data;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public String getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
	}
	public int getNowPage() {
		return nowPage;
	}
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	public List<PhotoAlbumBackList> getData() {
		return data;
	}
	public void setData(List<PhotoAlbumBackList> data) {
		this.data = data;
	}
	
	
}
