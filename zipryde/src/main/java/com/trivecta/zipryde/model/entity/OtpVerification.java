package com.trivecta.zipryde.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the otp_verification database table.
 * 
 */
@Entity
@Table(name="OTP_VERIFICATION")
@NamedQueries ({
	@NamedQuery(name="OtpVerification.findByMobile", 
			query="SELECT o FROM OtpVerification o where o.mobileNumber = :mobileNumber") ,
	@NamedQuery(name="OtpVerification.verifyByMobile", 
	query="SELECT o FROM OtpVerification o where o.mobileNumber = :mobileNumber and o.otp = :otp and "
			+ " o.validUntil >= :validUntil")
})
public class OtpVerification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String mobileNumber;

	private String otp;

	@Temporal(TemporalType.TIMESTAMP)
	private Date validUntil;

	public OtpVerification() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOtp() {
		return this.otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Date getValidUntil() {
		return this.validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

}