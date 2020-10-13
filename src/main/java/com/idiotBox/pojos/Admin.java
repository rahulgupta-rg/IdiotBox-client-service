package com.idiotBox.pojos;

public class Admin {
	private Integer adminId;
	private String fName;
	private String lName;
	private String email;
	private String aEmail;
	private String phone;
	private String password;

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getaEmail() {
		return aEmail;
	}

	public void setaEmail(String aEmail) {
		this.aEmail = aEmail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Admin(String fName, String lName, String email, String aEmail, String phone, String password) {
		super();
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.aEmail = aEmail;
		this.phone = phone;
		this.password = password;
	}

	public Admin(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public Admin() {
		super();
	}

	@Override
	public String toString() {
		return "Admin [adminId=" + adminId + ", fName=" + fName + ", lName=" + lName + ", email=" + email + ", aEmail="
				+ aEmail + ", phone=" + phone + ", password=" + password + "]";
	}

}