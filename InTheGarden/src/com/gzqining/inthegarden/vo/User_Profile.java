package com.gzqining.inthegarden.vo;

import com.gzqining.inthegarden.vo.user_profile.Birthday;
import com.gzqining.inthegarden.vo.user_profile.Birthplace;
import com.gzqining.inthegarden.vo.user_profile.Career;
import com.gzqining.inthegarden.vo.user_profile.Degrees;
import com.gzqining.inthegarden.vo.user_profile.Drinking;
import com.gzqining.inthegarden.vo.user_profile.Faith;
import com.gzqining.inthegarden.vo.user_profile.Height;
import com.gzqining.inthegarden.vo.user_profile.Income;
import com.gzqining.inthegarden.vo.user_profile.Language;
import com.gzqining.inthegarden.vo.user_profile.Marriage;
import com.gzqining.inthegarden.vo.user_profile.Smoking;
import com.gzqining.inthegarden.vo.user_profile.TextBean;
import com.gzqining.inthegarden.vo.user_profile.Weight;


public class User_Profile {
	private Birthday birthday;
	private Birthplace birthplace;
	private Career career;
	private Degrees degrees;
	private Drinking drinking;
	private Faith faith;
	private Height height;
	private Income income;
	private Language language;
	private Marriage marriage;
	private Smoking smoking;
	private TextBean text;
	private Weight weight;
	
	public Birthday getBirthday() {
		return birthday;
	}
	public void setBirthday(Birthday birthday) {
		this.birthday = birthday;
	}
	public Birthplace getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(Birthplace birthplace) {
		this.birthplace = birthplace;
	}
	public Career getCareer() {
		return career;
	}
	public void setCareer(Career career) {
		this.career = career;
	}
	public Degrees getDegrees() {
		return degrees;
	}
	public void setDegrees(Degrees degrees) {
		this.degrees = degrees;
	}
	public Drinking getDrinking() {
		return drinking;
	}
	public void setDrinking(Drinking drinking) {
		this.drinking = drinking;
	}
	public Faith getFaith() {
		return faith;
	}
	public void setFaith(Faith faith) {
		this.faith = faith;
	}
	public Height getHeight() {
		return height;
	}
	public void setHeight(Height height) {
		this.height = height;
	}
	public Income getIncome() {
		return income;
	}
	public void setIncome(Income income) {
		this.income = income;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public Marriage getMarriage() {
		return marriage;
	}
	public void setMarriage(Marriage marriage) {
		this.marriage = marriage;
	}
	public Smoking getSmoking() {
		return smoking;
	}
	public void setSmoking(Smoking smoking) {
		this.smoking = smoking;
	}
	public TextBean getText() {
		return text;
	}
	public void setText(TextBean text) {
		this.text = text;
	}
	public Weight getWeight() {
		return weight;
	}
	public void setWeight(Weight weight) {
		this.weight = weight;
	}
	
}
