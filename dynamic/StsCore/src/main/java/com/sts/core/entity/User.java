package com.sts.core.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String email;
	private String avtarImage;
	private String firstName;
	private String middleName;
	private String lastName;
	private String userName;
	private String aboutMe;
	private String gender;
	private Image photo;
	private String contact;
	private String verificationCode;
	private boolean verified;
	private String password;

	// transient attributes
	private String photoUrl;
	private UserType type;
	private boolean firstLogin;

	public User() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement(name = "photo")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "PHOTOID", unique = false, nullable = true, updatable = true)
	public Image getPhoto() {
		return photo;
	}

	public void setPhoto(Image photo) {
		this.photo = photo;
	}

	public String getAvtarImage() {
		return avtarImage;
	}

	public void setAvtarImage(String avtarImage) {
		this.avtarImage = avtarImage;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the aboutMe
	 */
	public String getAboutMe() {
		return aboutMe;
	}

	/**
	 * @param aboutMe
	 *            the aboutMe to set
	 */
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	@Transient
	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	/**
	 * @return the type
	 */
	@Enumerated(EnumType.ORDINAL)
	public UserType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(UserType type) {
		this.type = type;
	}

	@PrePersist
	public void prePersist() {
		if (getType() == null)
			setType(UserType.CUSTOMER);
	}

	/**
	 * @return the firstLogin
	 */
	public boolean isFirstLogin() {
		return firstLogin;
	}

	/**
	 * @param firstLogin
	 *            the firstLogin to set
	 */
	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
