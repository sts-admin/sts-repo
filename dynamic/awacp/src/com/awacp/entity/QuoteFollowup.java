package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: QuoteFollowup
 *
 */
@Entity
@XmlRootElement

@NamedQueries({
		@NamedQuery(name = "QuoteFollowup.findAll", query = "SELECT qf FROM QuoteFollowup qf WHERE qf.archived = 'false' AND qf.takeoffId =:takeoffId") })
public class QuoteFollowup extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String createdByUserCode; // Code of the User created this record.
	private String followupText; // Code of the user update this record.
	private Long takeoffId;
	private Long worksheetId;

	// Transient

	private boolean mailSent;

	public QuoteFollowup() {
		super();
	}

	@Lob
	@Column(nullable = false, length = 1024)
	public String getFollowupText() {
		return followupText;
	}

	public void setFollowupText(String followupText) {
		this.followupText = followupText;
	}

	@NotNull
	@Column(nullable = false, length = 10)
	public String getCreatedByUserCode() {
		return createdByUserCode;
	}

	public void setCreatedByUserCode(String createdByUserCode) {
		this.createdByUserCode = createdByUserCode;
	}

	public Long getTakeoffId() {
		return takeoffId;
	}

	public void setTakeoffId(Long takeoffId) {
		this.takeoffId = takeoffId;
	}

	public Long getWorksheetId() {
		return worksheetId;
	}

	public void setWorksheetId(Long worksheetId) {
		this.worksheetId = worksheetId;
	}

	@Transient
	public boolean isMailSent() {
		return mailSent;
	}

	public void setMailSent(boolean mailSent) {
		this.mailSent = mailSent;
	}

}
