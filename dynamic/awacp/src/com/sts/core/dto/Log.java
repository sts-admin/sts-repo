package com.sts.core.dto;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlRootElement;

import com.awacp.util.Action;

@XmlRootElement
public class Log {
	private String section; // Entity class name
	private String description; // Change description like insert/update with
								// additional message
	private Calendar dateCreated; // Activity datex
	private Long createdBy; // User id
	private String userCode; // Unique user code
	private Long version; // Version number;a
	private com.awacp.util.Action action;

	/**
	 * 
	 */
	public Log() {
		super();
	}

	public Log(String section, String description, Calendar dateCreated, Long createdBy, String userCode, Long version,
			Action action) {
		super();
		this.section = section;
		this.description = description;
		this.dateCreated = dateCreated;
		this.createdBy = createdBy;
		this.userCode = userCode;
		this.version = version;
		this.action = action;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Calendar dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public com.awacp.util.Action getAction() {
		return action;
	}

	public void setAction(com.awacp.util.Action action) {
		this.action = action;
	}

}
