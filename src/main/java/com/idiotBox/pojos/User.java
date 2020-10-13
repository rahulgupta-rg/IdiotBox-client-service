package com.idiotBox.pojos;

import java.util.Date;

public class User {

	private Integer userId;
	private String fName;
	

	private String lName;
	private String  email;
	private String aEmail;
	private String password;
	private Date birthDate;
	private String gender;
	private String phone;
	private String twoFactAuthStatus;
	
	public User() {
		super();
	}
	
	public User(String fName, String lName, String email, String aEmail, String password, Date birthDate, String gender, String phone) {
		super();
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.aEmail = aEmail;
		this.password = password;
		this.birthDate = birthDate;
		this.gender = gender;
		this.phone = phone;
	}
	
	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAEmail() {
		return aEmail;
	}
	
	public void setAEmail(String aEmail) {
		this.aEmail = aEmail;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getlName() {
		return lName;
	}
	
	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getTwoFactAuthStatus() {
		return twoFactAuthStatus;
	}

	public void setTwoFactAuthStatus(String twoFactAuthStatus) {
		this.twoFactAuthStatus = twoFactAuthStatus;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", fName=" + fName + ", lName=" + lName + ", email=" + email + ", aEmail="
				+ aEmail + ", password=" + password + ", birthDate=" + birthDate + ", gender=" + gender + ", phone=" + phone + "]";
	}
	
}