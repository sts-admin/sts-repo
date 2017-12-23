package com.awacp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;
import com.sts.core.entity.File;

/**
 * Entity implementation class for Entity: SiteInfo
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "SiteInfo.findAll", query = "SELECT mi FROM SiteInfo mi WHERE mi.archived = 'false' ORDER BY mi.dateCreated DESC")

})
public class SiteInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String companyName;
	private String frontWebsite;
	private String backendApp;
	private String officeAddress;
	private String phoneNumber;
	private String faxNumber;
	private String emailAddress;
	private String clockOneLabel;
	private boolean clockOneActive;
	private String clockOneTimezone;

	private String clockTwoLabel;
	private boolean clockTwoActive;
	private String clockTwoTimezone;

	private String smtpHost;
	private String smtpPort;

	private File loginLogo; // 517 X 114 px
	private File headerLogo; // 307 X 77 px
	private File emailLogo; // 408 X 86 px

	private String loginLogoUrl;
	private String headerLogoUrl;
	private String emailLogoUrl;

	public SiteInfo() {
		super();
	}

	public String getCompanyName() {
		return companyName == null ? "ALBERT WEISS AIR CONDITIONING PRODUCTS INC." : companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFrontWebsite() {
		return frontWebsite == null ? "http://www.awacp.com" : frontWebsite;
	}

	public void setFrontWebsite(String frontWebsite) {
		this.frontWebsite = frontWebsite;
	}

	public String getBackendApp() {
		return backendApp == null ? "http://www.awacp.org" : backendApp;
	}

	public void setBackendApp(String backendApp) {
		this.backendApp = backendApp;
	}

	public String getOfficeAddress() {
		return officeAddress == null ? "270 MADISON AVENUE, SUITE 1805, NEW YORK, N.Y. 10016" : officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber == null ? "((212) 679-8550)" : phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber == null ? "((212) 213-5067)" : faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getEmailAddress() {
		return emailAddress == null ? "edwgs@awacp.com" : emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getClockOneLabel() {
		return clockOneLabel == null ? "INDIA" : clockOneLabel;
	}

	public void setClockOneLabel(String clockOneLabel) {
		this.clockOneLabel = clockOneLabel;
	}

	public boolean isClockOneActive() {
		return clockOneActive;
	}

	public void setClockOneActive(boolean clockOneActive) {
		this.clockOneActive = clockOneActive;
	}

	public String getClockOneTimezone() {
		return clockOneTimezone == null ? "5.5" : clockOneTimezone;
	}

	public void setClockOneTimezone(String clockOneTimezone) {
		this.clockOneTimezone = clockOneTimezone;
	}

	public String getClockTwoLabel() {
		return clockTwoLabel == null ? "NEW YORK" : clockTwoLabel;
	}

	public void setClockTwoLabel(String clockTwoLabel) {
		this.clockTwoLabel = clockTwoLabel;
	}

	public boolean isClockTwoActive() {
		return clockTwoActive;
	}

	public void setClockTwoActive(boolean clockTwoActive) {
		this.clockTwoActive = clockTwoActive;
	}

	public String getClockTwoTimezone() {
		return clockTwoTimezone == null ? "-5.0" : clockTwoTimezone;
	}

	public void setClockTwoTimezone(String clockTwoTimezone) {
		this.clockTwoTimezone = clockTwoTimezone;
	}

	public String getSmtpHost() {
		return smtpHost == null ? "smtp.office365.com" : smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort == null ? "587" : smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	@XmlElement(name = "loginLogo")
	@OneToOne(optional = true, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name = "LOGINLOGOID", unique = true, nullable = true, updatable = true)
	public File getLoginLogo() {
		return loginLogo;
	}

	public void setLoginLogo(File loginLogo) {
		this.loginLogo = loginLogo;
	}

	@XmlElement(name = "headerLogo")
	@OneToOne(optional = true, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name = "HEADERLOGOID", unique = true, nullable = true, updatable = true)
	public File getHeaderLogo() {
		return headerLogo;
	}

	public void setHeaderLogo(File headerLogo) {
		this.headerLogo = headerLogo;
	}

	@XmlElement(name = "emailLogo")
	@OneToOne(optional = true, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	@JoinColumn(name = "EMAILLOGOID", unique = true, nullable = true, updatable = true)
	public File getEmailLogo() {
		return emailLogo;
	}

	public void setEmailLogo(File emailLogo) {
		this.emailLogo = emailLogo;
	}

	@Transient
	public String getLoginLogoUrl() {
		return loginLogoUrl;
	}

	@Transient
	public String getHeaderLogoUrl() {
		return headerLogoUrl;
	}

	@Transient
	public String getEmailLogoUrl() {
		return emailLogoUrl;
	}

	public void setLoginLogoUrl(String loginLogoUrl) {
		this.loginLogoUrl = loginLogoUrl;
	}

	public void setHeaderLogoUrl(String headerLogoUrl) {
		this.headerLogoUrl = headerLogoUrl;
	}

	public void setEmailLogoUrl(String emailLogoUrl) {
		this.emailLogoUrl = emailLogoUrl;
	}

	@PrePersist
	public void prePersist() {
		if (getFrontWebsite() == null) {
			setFrontWebsite("www.awacp.com");
		}
		if (getOfficeAddress() == null) {
			setOfficeAddress("270 MADISON AVENUE, SUITE 1805, NEW YORK, N.Y. 10016");
		}
		if (getPhoneNumber() == null) {
			setPhoneNumber("(212) 679-8550");
		}
		if (getFaxNumber() == null) {
			setFaxNumber("(212) 213-5067");
		}
		if (getEmailAddress() == null) {
			setEmailAddress("edwgs@awacp.com");
		}
	}

}
