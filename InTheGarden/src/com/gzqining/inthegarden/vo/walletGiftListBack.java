package com.gzqining.inthegarden.vo;

import java.util.List;

public class walletGiftListBack {
	private String message;
	private String code;
	private int count;
	private int totalPages;
	private int totalRows;
	private int nowPage;
	private String html;
	private List<walletGiftListBean> data;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	public int getNowPage() {
		return nowPage;
	}
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public List<walletGiftListBean> getData() {
		return data;
	}
	public void setData(List<walletGiftListBean> data) {
		this.data = data;
	}
}
