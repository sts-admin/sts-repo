package com.awacp.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: JobDocument
 *
 */
@Entity
@XmlRootElement
public class JobDocument extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String orderId;
	private String documentName;
	private String columnValue;
	private String documentType;
	private String userCode;
	private Calendar dateCreated;

	public JobDocument() {
		super();
	}

	@NotNull
	@Column(nullable = false, length = 20)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@NotNull
	@Column(nullable = false, length = 100)
	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	@NotNull
	@Column(nullable = false, length = 200)
	public String getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

	@NotNull
	@Column(nullable = false, length = 4)
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
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
	public Calendar getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Calendar dateCreated) {
		this.dateCreated = dateCreated;
	}

}
