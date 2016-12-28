package com.awacp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

import com.sts.core.entity.BaseEntity;
import com.sts.core.entity.File;

/**
 * Entity implementation class for Entity: Trucker
 *
 */
@Entity
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Trucker.listAll", query = "SELECT b FROM Trucker b WHERE b.archived = 'false'"),
		@NamedQuery(name = "Trucker.countAll", query = "SELECT COUNT(b.id) FROM Trucker b WHERE b.archived = 'false'"),
		@NamedQuery(name = "Trucker.getByEmail", query = "SELECT b FROM Trucker b WHERE b.archived = 'false' AND LOWER(b.email) = :email") })
public class Trucker extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String name; // required
	private String code;
	private String login;
	private String password;
	private String trackingUrl;
	private String phone;
	private String email; // required
	private String website; // required
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	private File logo;

	public Trucker() {
		super();
	}

	@Column(length = 20)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@NotNull
	@Column(nullable = false, length = 100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(length = 150)
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@NotNull
	@Column(nullable = false, length = 10)
	public String getCreatedByUserCode() {
		return createdByUserCode;
	}

	public void setCreatedByUserCode(String createdByUserCode) {
		this.createdByUserCode = createdByUserCode;
	}

	@Column(length = 10)
	public String getUpdatedByUserCode() {
		return updatedByUserCode;
	}

	public void setUpdatedByUserCode(String updatedByUserCode) {
		this.updatedByUserCode = updatedByUserCode;
	}

	@Column(nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 20)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 20)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(length = 100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(length = 255)
	public String getTrackingUrl() {
		return trackingUrl;
	}

	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}

	@XmlElement(name = "logo")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "LOGOID", unique = false, nullable = true, updatable = true)
	public File getLogo() {
		return logo;
	}

	public void setLogo(File logo) {
		this.logo = logo;
	}

	@PrePersist
	public void setDefaults() {

		if (this.getUpdatedByUserCode() == null) {
			this.updatedByUserCode = "";
		}
		if (this.getPhone() == null) {
			this.phone = "";
		}

	}

}
