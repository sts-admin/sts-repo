package com.awacp.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;
import com.sts.core.entity.File;

/**
 * Entity implementation class for Entity: PageSetting
 *
 */
@Entity
@XmlRootElement
public class AppSetting extends BaseEntity {

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

	File loginLogo; // 517 X 114 px
	File headerLogo; // 307 X 77 px
	File emailLogo; // 408 X 86 px

	public AppSetting() {
		super();
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFrontWebsite() {
		return frontWebsite;
	}

	public void setFrontWebsite(String frontWebsite) {
		this.frontWebsite = frontWebsite;
	}

	public String getBackendApp() {
		return backendApp;
	}

	public void setBackendApp(String backendApp) {
		this.backendApp = backendApp;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getClockOneLabel() {
		return clockOneLabel;
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
		return clockOneTimezone;
	}

	public void setClockOneTimezone(String clockOneTimezone) {
		this.clockOneTimezone = clockOneTimezone;
	}

	public String getClockTwoLabel() {
		return clockTwoLabel;
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
		return clockTwoTimezone;
	}

	public void setClockTwoTimezone(String clockTwoTimezone) {
		this.clockTwoTimezone = clockTwoTimezone;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public File getLoginLogo() {
		return loginLogo;
	}

	public void setLoginLogo(File loginLogo) {
		this.loginLogo = loginLogo;
	}

	public File getHeaderLogo() {
		return headerLogo;
	}

	public void setHeaderLogo(File headerLogo) {
		this.headerLogo = headerLogo;
	}

	public File getEmailLogo() {
		return emailLogo;
	}

	public void setEmailLogo(File emailLogo) {
		this.emailLogo = emailLogo;
	}

}
