package com.sts.core.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StsCoreResponse {

	private String statusMessage;
	private String userName;
	private String userFullName;
	private String statusCode;
	private String signedUpUserId;

	/**
	 * 
	 */
	public StsCoreResponse() {
		super();
	}

	/**
	 * @param statusMessage
	 */
	public StsCoreResponse(String statusMessage) {
		super();
		this.statusMessage = statusMessage;
	}

	/**
	 * @param userName
	 * @param statusCode
	 */
	public StsCoreResponse(String userName, String statusCode) {
		super();
		this.userName = userName;
		this.statusCode = statusCode;
	}
	
	

	/**
	 * @param signedUpUserId
	 * @param userName
	 * @param statusCode
	 */
	public StsCoreResponse(String userName, String userFullName, String statusCode) {
		super();
		this.userName = userName;
		this.userFullName = userFullName;
		this.statusCode = statusCode;
	}

	/**
	 * @param userName
	 * @param signedUpUserId
	 * @param statusMessage
	 * @param statusCode
	 */
	public StsCoreResponse(String userName, String signedUpUserId, String statusMessage, String statusCode) {
		super();
		this.userName = userName;
		this.signedUpUserId = signedUpUserId;
		this.statusMessage = statusMessage;
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage
	 *            the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
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
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the signedUpUserId
	 */
	public String getSignedUpUserId() {
		return signedUpUserId;
	}

	/**
	 * @param signedUpUserId
	 *            the signedUpUserId to set
	 */
	public void setSignedUpUserId(String signedUpUserId) {
		this.signedUpUserId = signedUpUserId;
	}

	/**
	 * @return the userFullName
	 */
	public String getUserFullName() {
		return userFullName;
	}

	/**
	 * @param userFullName the userFullName to set
	 */
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	

}
