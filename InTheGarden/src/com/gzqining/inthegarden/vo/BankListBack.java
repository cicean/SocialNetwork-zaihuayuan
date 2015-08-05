package com.gzqining.inthegarden.vo;

import java.util.List;

public class BankListBack {
	private String message;
	private String code;
	private List<bankInfo> data;

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

	public List<bankInfo> getData() {
		return data;
	}

	public void setData(List<bankInfo> data) {
		this.data = data;
	}

}
