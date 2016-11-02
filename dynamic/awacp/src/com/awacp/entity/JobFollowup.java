package com.awacp.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: JobFollowup
 *
 */
@Entity
@XmlRootElement
public class JobFollowup extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String jobId; // length 10
	private Calendar dateCreated;
	private String comment; // length 250

	private Calendar nextRemindDate;
	private boolean status;
	private String userCode; // length 10
	private boolean remiderDismissed;

	public JobFollowup() {
		super();
	}

	@NotNull
	@Column(nullable = false, length = 10)
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@NotNull
	@Column(nullable = false)
	public Calendar getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Calendar dateCreated) {
		this.dateCreated = dateCreated;
	}

	@NotNull
	@Column(nullable = false, length = 250)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@NotNull
	@Column(nullable = false)
	public Calendar getNextRemindDate() {
		return nextRemindDate;
	}

	public void setNextRemindDate(Calendar nextRemindDate) {
		this.nextRemindDate = nextRemindDate;
	}

	@NotNull
	@Column(nullable = false)
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@NotNull
	@Column(nullable = false, length = 10)
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@NotNull
	@Column(nullable = false)
	public boolean isRemiderDismissed() {
		return remiderDismissed;
	}

	public void setRemiderDismissed(boolean remiderDismissed) {
		this.remiderDismissed = remiderDismissed;
	}

}
