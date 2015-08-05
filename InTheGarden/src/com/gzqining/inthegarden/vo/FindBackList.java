package com.gzqining.inthegarden.vo;

import com.gzqining.inthegarden.vo.personInfo.Follow_StateBean;
import com.gzqining.inthegarden.vo.personInfo.ProfileBean;

public class FindBackList {
	private String uid;
	private String nickname;
	private String intro;
	private Long last_login_time;
	private String sex;
	private String follower_count;
	private String distance;
	private String is_vip;
	private String avatar_original;
	private String avatar_big;
	private String avatar_middle;
	private String avatar_small;
	private String avatar_tiny;
	private Follow_StateBean follow_state;
	private ProfileBean profile;
	
	
	
	
	public String getIs_vip() {
		return is_vip;
	}
	public void setIs_vip(String is_vip) {
		this.is_vip = is_vip;
	}
	public Follow_StateBean getFollow_state() {
		return follow_state;
	}
	public void setFollow_state(Follow_StateBean follow_state) {
		this.follow_state = follow_state;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public Long getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(Long last_login_time) {
		this.last_login_time = last_login_time;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getFollower_count() {
		return follower_count;
	}
	public void setFollower_count(String follower_count) {
		this.follower_count = follower_count;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getAvatar_original() {
		return avatar_original;
	}
	public void setAvatar_original(String avatar_original) {
		this.avatar_original = avatar_original;
	}
	public String getAvatar_big() {
		return avatar_big;
	}
	public void setAvatar_big(String avatar_big) {
		this.avatar_big = avatar_big;
	}
	public String getAvatar_middle() {
		return avatar_middle;
	}
	public void setAvatar_middle(String avatar_middle) {
		this.avatar_middle = avatar_middle;
	}
	public String getAvatar_small() {
		return avatar_small;
	}
	public void setAvatar_small(String avatar_small) {
		this.avatar_small = avatar_small;
	}
	public String getAvatar_tiny() {
		return avatar_tiny;
	}
	public void setAvatar_tiny(String avatar_tiny) {
		this.avatar_tiny = avatar_tiny;
	}
	public ProfileBean getProfile() {
		return profile;
	}
	public void setProfile(ProfileBean profile) {
		this.profile = profile;
	}
	
	
}
