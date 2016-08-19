package com.sts.core.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: Address
 *
 */
@Entity
@XmlRootElement
public class Address extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String placeId;
	private String street;
	private String appartment;
	private String city;
	private State state;
	private Country country;
	private String zipCode;
	private Long userId;
	private Double longitude;
	private Double latitude;

	public Address() {
		super();
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
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

	@XmlElement(name = "state")
	@OneToOne(optional = false, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "STATEID", unique = false, nullable = false, updatable = true)
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@XmlElement(name = "country")
	@OneToOne(optional = false, cascade = { CascadeType.DETACH })
	@JoinColumn(name = "COUNTRYID", unique = false, nullable = false, updatable = true)
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the longitude
	 */
	@Column(precision = 9, scale = 6)
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	@Column(precision = 9, scale = 6)
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the apartment
	 */
	public String getAppartment() {
		return appartment;
	}

	/**
	 * @param appartment
	 *            the apartment to set
	 */
	public void setAppartment(String appartment) {
		this.appartment = appartment;
	}

}
