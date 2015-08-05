package com.gzqining.inthegarden.vo;

public class LoginBack {
	private String message;
	private int code;
	private String oauth_token;
	private String oauth_token_secret;
	private String uid;
	//融云token
	private String token;
	//是否vip 0：否 1：是
	private int is_vip;
	//是否认证  0：未认证 1：审核中 2：已认证
	private int is_approve;
	//是否第一次提现 0：是 1：否
	private int is_first;
	
	public String Getmessage() {
    	return message;
    }
    
    public void Setmessage(String message) {
    	this.message = message;
    }
    
    public  int Getcode() {
    	return code;
    }
    
    public void Setcode(int code) {
    	this.code = code;
    }
    
    public String Getoauth_token() {
    	return oauth_token;
    }
    
    public void Setoauth_token(String oauth_token) {
    	this.oauth_token = oauth_token;
    }
    
    public String Getoauth_token_secret() {
    	return oauth_token_secret;
    }
    
    public void Setoauth_token_secret(String oauth_token_secret) {
    	this.oauth_token_secret = oauth_token_secret;
    }
    
    public String Getuid() {
    	return uid;
    }
    
    public void Setuid(String uid) {
    	this.uid = uid;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getIs_vip() {
		return is_vip;
	}

	public void setIs_vip(int is_vip) {
		this.is_vip = is_vip;
	}

	public int getIs_approve() {
		return is_approve;
	}

	public void setIs_approve(int is_approve) {
		this.is_approve = is_approve;
	}

	public int getIs_first() {
		return is_first;
	}

	public void setIs_first(int is_first) {
		this.is_first = is_first;
	}
    
}
