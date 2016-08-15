package com.sts.core.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: PasswordResetHistory
 *
 */
@Entity
@NamedQuery(name = "PasswordResetHistory.findByOTP", query = "SELECT prh FROM PasswordResetHistory prh where prh.otp = :otp and prh.archived=:archived")
@XmlRootElement
public class PasswordResetHistory extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String otp;
	private String oldPassword;
	private String newPassword;
	private User user;

	public PasswordResetHistory() {
		super();
	}

	/**
	 * @param otp
	 * @param user
	 */
	public PasswordResetHistory(String otp, User user) {
		super();
		this.otp = otp;
		this.user = user;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@XmlElement(name = "user")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "EXISTINGUSERID", unique = false, nullable = true, updatable = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
