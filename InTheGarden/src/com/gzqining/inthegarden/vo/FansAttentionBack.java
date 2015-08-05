package com.gzqining.inthegarden.vo;

import java.util.List;

import com.gzqining.inthegarden.vo.personInfo.Followers;

public class FansAttentionBack {
	private int code;
	private String message;
	List<Followers> followers;
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Followers> getList() {
		return followers;
	}
	public void setList(List<Followers> followers) {
		this.followers = followers;
	}
	
}
