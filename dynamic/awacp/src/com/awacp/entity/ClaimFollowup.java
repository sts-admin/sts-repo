package com.awacp.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: ClaimFollowup
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ClaimFollowup.getByClaim", query = "SELECT cf FROM ClaimFollowup cf WHERE cf.archived = 'false' AND cf.claimId = :claimId AND LOWER(cf.claimSource) = :source ORDER BY cf.dateCreated DESC") })
public class ClaimFollowup extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long claimId; // length 10
	private Calendar dateCreated;
	private String comment; // length 250

	private Calendar nextRemindDate;
	private boolean status;
	private String userCode; // length 10
	private boolean reminderDismissed;

	private String claimSource;

	public ClaimFollowup() {
		super();
	}

	@NotNull
	@Column(nullable = false, length = 10)
	public Long getClaimId() {
		return claimId;
	}

	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}

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

	public boolean isReminderDismissed() {
		return reminderDismissed;
	}

	public void setReminderDismissed(boolean reminderDismissed) {
		this.reminderDismissed = reminderDismissed;
	}

	@Column(nullable = false, length = 2)
	public String getClaimSource() {
		return claimSource;
	}

	public void setClaimSource(String claimSource) {
		this.claimSource = claimSource;
	}

}
