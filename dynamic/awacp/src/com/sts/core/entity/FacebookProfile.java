package com.sts.core.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "FacebookProfile.Login", query = "SELECT fp FROM FacebookProfile fp WHERE fp.archived = 'false' AND LOWER(fp.email) = :email AND fp.verified = :verified "),
		@NamedQuery(name = "FacebookProfile.findByUserId", query = "SELECT fp FROM FacebookProfile fp WHERE fp.archived = 'false' AND fp.verified = 'true' AND fp.userId =:userId") })
public class FacebookProfile extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String facebookId;
	private String facebookToken;
	private String email;
	private File photo;
	private boolean verified;
	private String userName;
	private Long userId;

	public FacebookProfile() {
		super();
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	@Lob
	@Column(name = "FACEBOOKTOKEN", length = 512)
	public String getFacebookToken() {
		return facebookToken;
	}

	public void setFacebookToken(String facebookToken) {
		this.facebookToken = facebookToken;
	}

	@XmlElement(name = "photo")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "PHOTOID", unique = false, nullable = true, updatable = true)
	public File getPhoto() {
		return photo;
	}

	public void setPhoto(File photo) {
		this.photo = photo;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@NotNull
	@Column(nullable = false, length = 100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@NotNull
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
