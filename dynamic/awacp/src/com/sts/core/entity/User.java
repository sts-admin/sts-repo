package com.sts.core.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
		@NamedQuery(name = "User.findAllDeletedUsers", query = "SELECT u FROM User u WHERE u.archived = 'true' ORDER BY u.dateCreated DESC"),
		@NamedQuery(name = "User.Login", query = "SELECT u FROM User u WHERE u.archived = 'false' AND (LOWER(u.email) = :email OR LOWER(u.userName) = :userName) AND u.verified = :verified "),
		@NamedQuery(name = "User.findUserByNameOrEmail", query = "SELECT u FROM User u WHERE u.archived = 'false' AND (LOWER(u.email) = :email OR LOWER(u.userName) = :userName)"),
		@NamedQuery(name = "User.findUserByEmail", query = "SELECT u FROM User u WHERE lower(u.email) = :email"),
		@NamedQuery(name = "User.findUserCode", query = "SELECT u FROM User u WHERE lower(u.userCode) = :userCode"),
		@NamedQuery(name = "User.getCode", query = "SELECT u.userCode FROM User u WHERE u.archived = 'false' AND (LOWER(u.email) = :email OR LOWER(u.userName) = :userName)"),
		@NamedQuery(name = "User.filterByNameMatch", query = "SELECT new com.sts.core.entity.User(u.id, u.firstName) FROM User u WHERE u.archived = 'false' AND LOWER(u.firstName) LIKE :keyword"),
		@NamedQuery(name = "User.listOnlineUsers", query = "SELECT new com.sts.core.dto.UserDTO(u.id, u.userName, u.userCode, u.firstName, u.middleName, u.lastName, u.avtarImage, u.photo.id, u.online) FROM User u WHERE u.archived = 'false' AND u.online = 'true' ORDER BY u.onlineTime DESC"),
		@NamedQuery(name = "User.listOfflineUsers", query = "SELECT new com.sts.core.dto.UserDTO(u.id, u.userName, u.userCode, u.firstName, u.middleName, u.lastName, u.avtarImage, u.photo.id, u.online) FROM User u WHERE u.archived = 'false' AND u.online = 'false' ORDER BY u.dateCreated DESC")

})
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
	private File photo;
	private String contact;
	private String verificationCode;
	private boolean verified;
	private String password;
	private String userCode;
	private String role;
	private boolean firstLogin;
	private boolean online;
	private boolean deleted;
	private Calendar onlineTime;

	private Set<Permission> permissions;

	// transient attributes
	private String photoUrl;
	private String[] permissionArray;
	private boolean permissionChanged;

	public User() {
		super();
	}

	public User(Long id, String firstName) {
		this.setId(id);
		this.firstName = firstName;
	}

	public User(Long id, String firstName, String lastName, String userName, String userCode, String password,
			String email, String role, Calendar dateCreated) {
		this.setId(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.userCode = userCode;
		this.password = password;
		this.email = email;
		this.role = role;
		this.setDateCreated(dateCreated);

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
	@OneToOne(optional = true, cascade = CascadeType.DETACH)
	@JoinColumn(name = "PHOTOID", unique = false, nullable = true, updatable = true)
	public File getPhoto() {
		return photo;
	}

	public void setPhoto(File photo) {
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
	@Column(length = 20)
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
	@Column(length = 20)
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
	@Column(length = 10)
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

	@PrePersist
	public void prePersist() {
		if (getRole() == null) {
			setRole(RoleType.GUEST.getName());
		}
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

	@Column(length = 20)
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

	@Column(length = 50)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(length = 10)
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

	@XmlElement(name = "permissions")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "USER_PERMISSION", joinColumns = @JoinColumn(name = "USERID"), inverseJoinColumns = @JoinColumn(name = "PERMISSIONID"))
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Column(length = 20)
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Transient
	public String[] getPermissionArray() {
		return permissionArray;
	}

	public void setPermissionArray(String[] permissionArray) {
		this.permissionArray = permissionArray;
	}

	@Transient
	public boolean isPermissionChanged() {
		return permissionChanged;
	}

	public void setPermissionChanged(boolean permissionChanged) {
		this.permissionChanged = permissionChanged;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Calendar getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Calendar onlineTime) {
		this.onlineTime = onlineTime;
	}

}
