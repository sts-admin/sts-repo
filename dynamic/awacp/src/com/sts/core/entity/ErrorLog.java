package com.sts.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: ErrorLog
 *
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "ErrorLog.findAll", query = "SELECT el FROM ErrorLog el WHERE el.archived = 'false'") 
})

@XmlRootElement
public class ErrorLog extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String serviceUrl;
	private String errorText;
	private String errorData;

	public ErrorLog() {
		super();
	}
	@NotNull
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Lob
	@Column(name = "serviceUrl", length = 1024)
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	@Lob
	@Column(name = "errorText", length = 1024)
	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	@Lob
	@Column(name = "errorData", length = 2048)
	public String getErrorData() {
		return errorData;
	}

	public void setErrorData(String errorData) {
		this.errorData = errorData;
	}
}
