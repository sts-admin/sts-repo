package com.sts.core.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: LinkedIn
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "LinkedInProfile.Login", query = "SELECT lp FROM LinkedInProfile lp WHERE lp.archived = 'false' AND LOWER(lp.email) = :email AND lp.verified = :verified "),
		@NamedQuery(name = "LinkedInProfile.findByUserId", query = "SELECT lp FROM LinkedInProfile lp WHERE lp.archived = 'false' AND lp.verified = 'true' AND lp.userId =:userId") 
})
public class LinkedInProfile extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String linkedInId;
	private String linkedInToken;
	private String phoneNumber;
	private Image photo;
	private boolean verified;
	private String userName;
	private Long userId;
	private String email;

	public LinkedInProfile() {
		super();
	}

	public String getLinkedInId() {
		return linkedInId;
	}

	public void setLinkedInId(String linkedInId) {
		this.linkedInId = linkedInId;
	}

	public String getLinkedInToken() {
		return linkedInToken;
	}

	public void setLinkedInToken(String linkedInToken) {
		this.linkedInToken = linkedInToken;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@XmlElement(name = "photo")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "PHOTOID", unique = false, nullable = true, updatable = true)
	public Image getPhoto() {
		return photo;
	}

	public void setPhoto(Image photo) {
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
	@Column(nullable = false)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@NotNull
	@Column(nullable = false, length = 100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
