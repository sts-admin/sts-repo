package com.sts.core.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDTO {
	private Long id;
	private String userName;
	private String fullName;
	private String userCode;

	private String firstName;

	private String middleName;
	private String lastName;
	private String avtarImage;
	private Long photoId;
	private String photoUrl;

	private int unreadMessageCount;

	public UserDTO(Long id, String userName, String userCode, String firstName, String middleName, String lastName,
			String avatarImage, Long photoId) {
		super();
		this.id = id;
		this.userName = userName;
		this.userCode = userCode;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.avtarImage = avatarImage;
		this.photoId = photoId;
	}

	/**
	 * 
	 */
	public UserDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName
	 *            the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public int getUnreadMessageCount() {
		return unreadMessageCount;
	}

	public void setUnreadMessageCount(int unreadMessageCount) {
		this.unreadMessageCount = unreadMessageCount;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAvtarImage() {
		return avtarImage;
	}

	public void setAvtarImage(String avtarImage) {
		this.avtarImage = avtarImage;
	}

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

}
