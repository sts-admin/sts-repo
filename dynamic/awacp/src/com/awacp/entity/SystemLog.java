package com.awacp.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: SystemLog
 *
 */
@Entity
@XmlRootElement
public class SystemLog extends BaseEntity {
	private String section;
	private String description;
	private String uc;

	// Transient
	private int pageNumber;
	private int pageSize;

	private Calendar fromDate;
	private Calendar toDate;

	private static final long serialVersionUID = 1L;

	public SystemLog() {
		super();
	}

	public SystemLog(String section, String description, Calendar dateCreated, Long createdById, String userCode,
			Long version) {
		setDateCreated(dateCreated);
		setCreatedById(createdById);
		setVersion(version);
		this.section = section;
		this.description = description;
		this.uc = userCode;
	}

	public String getUc() {
		return uc;
	}

	public void setUc(String uc) {
		this.uc = uc;
	}

	@Lob
	@Column(length = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	@Transient
	public int getPageNumber() {
		return pageNumber;
	}

	@Transient
	public int getPageSize() {
		return pageSize;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Transient
	public Calendar getFromDate() {
		return fromDate;
	}

	@Transient
	public Calendar getToDate() {
		return toDate;
	}

	public void setFromDate(Calendar fromDate) {
		this.fromDate = fromDate;
	}

	public void setToDate(Calendar toDate) {
		this.toDate = toDate;
	}

}
