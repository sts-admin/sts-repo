package com.sts.core.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u WHERE u.archived = 'false' ORDER BY u.dateCreated DESC"),
		@NamedQuery(name = "User.Login", query = "SELECT u FROM User u WHERE u.archived = 'false' AND (LOWER(u.email) = :email OR LOWER(u.userName) = :userName) AND u.verified = :verified "),
		@NamedQuery(name = "User.findUserByNameOrEmail", query = "SELECT u FROM User u WHERE u.archived = 'false' AND (LOWER(u.email) = :email OR LOWER(u.userName) = :userName)"),
		@NamedQuery(name = "User.findUserByEmail", query = "SELECT u FROM User u WHERE lower(u.email) = :email"),
		@NamedQuery(name = "User.findUserCode", query = "SELECT u FROM User u WHERE lower(u.userCode) = :userCode")

})
public class User extends BaseEntity {

	public static final String DUPLICATE_EMAIL = "duplicate_email";
	public static final String DUPLICATE_USERNAME = "duplicate_username";
	public static final String DUPLICATE_CODE = "duplicate_code";

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
	private String userCode;
	private Role role;
	private boolean firstLogin;
	private boolean online;

	// transient attributes
	private String photoUrl;

	public User() {
		super();
	}

	@NotNull
	@Column(nullable = false, length = 100)
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
	@NotNull
	@Column(nullable = false, length = 20)
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
	@NotNull
	@Column(nullable = false, length = 25)
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

	@XmlElement(name = "role")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "ROLE_NAME", unique = false, nullable = false, updatable = true)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@PrePersist
	public void prePersist() {
		if (getRole() == null) {
			role = new Role();
			role.setRoleName(RoleType.GUEST.getName());
			setRole(role);
		}
	}

	/**
	 * @return the firstLogin
	 */
	@Column(columnDefinition = "tinyint(1) default 1")
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

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

}
