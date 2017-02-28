package com.awacp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;
import com.sts.core.entity.Country;
import com.sts.core.entity.State;

/**
 * Entity implementation class for Entity: TaxEntry
 *
 */
@Entity
@XmlRootElement
public class TaxEntry extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Double rate;
	private Country country;
	private State state;
	private String city;
	
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public TaxEntry() {
		super();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@XmlElement(name = "state")
	@OneToOne(optional = false, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "STATEID", unique = false, nullable = true, updatable = true)
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@XmlElement(name = "country")
	@OneToOne(optional = false, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "COUNTRYID", unique = false, nullable = true, updatable = true)
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getCreatedByUserCode() {
		return createdByUserCode;
	}

	public void setCreatedByUserCode(String createdByUserCode) {
		this.createdByUserCode = createdByUserCode;
	}

	public String getUpdatedByUserCode() {
		return updatedByUserCode;
	}

	public void setUpdatedByUserCode(String updatedByUserCode) {
		this.updatedByUserCode = updatedByUserCode;
	}
	
	

}
