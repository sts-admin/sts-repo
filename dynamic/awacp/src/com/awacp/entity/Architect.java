package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;
import com.sts.core.entity.Country;
import com.sts.core.entity.State;

/**
 * Entity implementation class for Entity: Product
 *
 */
@Entity
@XmlRootElement
public class Architect extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String architectTitle;
	private Long salesPerson; // User ID
	private String street;
	private String city;
	private Country country;
	private State state;
	private String phone;
	private String fax;
	private String email;
	private String website;

	public Architect() {
		super();
	}

	
	@Column(nullable = true, length = 100)
	public String getArchitectTitle() {
		return architectTitle;
	}

	public void setArchitectTitle(String architectTitle) {
		this.architectTitle = architectTitle;
	}

	@NotNull
	@Column(nullable = false)
	public Long getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(Long salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
