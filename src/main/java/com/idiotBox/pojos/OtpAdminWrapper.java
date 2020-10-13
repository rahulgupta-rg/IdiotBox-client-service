package com.idiotBox.pojos;

public class OtpAdminWrapper {
	private String otp;
	private Admin admin;

	public OtpAdminWrapper() {
		super();
	}

	public OtpAdminWrapper(String otp, Admin admin) {
		super();
		this.otp = otp;
		this.admin = admin;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
}