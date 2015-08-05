package com.gzqining.inthegarden.vo;

import java.util.List;

import com.gzqining.inthegarden.vo.user_profile.User_Count_Info;
import com.gzqining.inthegarden.vo.user_profile.User_Follow_State;

public class UserInfoBack {
	private String message;
	private String code;
	private String login;
	private String uname;
	private String nickname;
	private String sex;
	private String location;
	private String is_audit;
	private String is_active;
	private String is_init;
	private String province;
	private String city;
	private String area;
	private String is_del;
	private String intro;
	private String avatar_original;
	
	private List<User_Profile> userprofile;
	private User_Count_Info count_info;
	private User_Follow_State follow_state;
	
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
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getIs_audit() {
		return is_audit;
	}
	public void setIs_audit(String is_audit) {
		this.is_audit = is_audit;
	}
	public String getIs_active() {
		return is_active;
	}
	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}
	public String getIs_init() {
		return is_init;
	}
	public void setIs_init(String is_init) {
		this.is_init = is_init;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getIs_del() {
		return is_del;
	}
	public void setIs_del(String is_del) {
		this.is_del = is_del;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getAvatar_original() {
		return avatar_original;
	}
	public void setAvatar_original(String avatar_original) {
		this.avatar_original = avatar_original;
	}
	public List<User_Profile> getUserprofile() {
		return userprofile;
	}
	public void setUserprofile(List<User_Profile> userprofile) {
		this.userprofile = userprofile;
	}
	public User_Count_Info getCount_info() {
		return count_info;
	}
	public void setCount_info(User_Count_Info count_info) {
		this.count_info = count_info;
	}
	public User_Follow_State getFollow_state() {
		return follow_state;
	}
	public void setFollow_state(User_Follow_State follow_state) {
		this.follow_state = follow_state;
	}
	
}
