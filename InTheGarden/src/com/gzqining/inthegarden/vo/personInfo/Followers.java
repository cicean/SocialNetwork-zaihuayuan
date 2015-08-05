package com.gzqining.inthegarden.vo.personInfo;


public class Followers {
	private String follow_id;
	private String uid;//用户ID
	private String nickname;//用户昵称
	private long last_login_time;//最后登录时间
	private String uname;//注册时自动生成的账号名
	private Follow_StateBean follow_state;//是否粉丝：0不是当前账号粉丝，1是（following，follower都为1则为互相关注）
	private ProfileBean profile;//用户具体信息
	private String avatar_big;//头像：200*200
	private String avatar_middle;//头像：100*100
	private String avatar_small;//头像：50*50
	private String sex;//性别：1 男，2 女
	private String intro;//心情
	private Count_InfoBean count_info;//关注数粉丝数
	public ProfileBean getProfile() {
		return profile;
	}
	public void setProfile(ProfileBean profile) {
		this.profile = profile;
	}
	public String getFollow_id() {
		
		return follow_id;
	}
	public void setFollow_id(String follow_id) {
		this.follow_id = follow_id;
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
	public long getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(long last_login_time) {
		this.last_login_time = last_login_time;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public Follow_StateBean getFollow_state() {
		return follow_state;
	}
	public void setFollow_state(Follow_StateBean follow_state) {
		this.follow_state = follow_state;
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
		//头像：50*50
		return avatar_small;
	}
	public void setAvatar_small(String avatar_small) {
		this.avatar_small = avatar_small;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public Count_InfoBean getCount_info() {
		return count_info;
	}
	public void setCount_info(Count_InfoBean count_info) {
		this.count_info = count_info;
	}
	
}
