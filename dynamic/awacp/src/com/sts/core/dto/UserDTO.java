package com.sts.core.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDTO {
	private Long userId;
	private String userName;
	private String fullName;
	
	
	

	/**
	 * 
	 */
	public UserDTO() {
		super();
	}

	/**
	 * @param userId
	 * @param userName
	 * @param fullName
	 */
	public UserDTO(Long userId, String userName, String fullName) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.fullName = fullName;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
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

}
