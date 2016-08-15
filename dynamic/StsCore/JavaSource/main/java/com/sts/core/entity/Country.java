package com.sts.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.ReadOnly;

/**
 * Entity implementation class for Entity: Country
 *
 */
@Entity
@ReadOnly
@NamedQuery(name = "Country.findAll", query = "SELECT c FROM Country c ORDER BY c.id ASC")
@XmlRootElement
public class Country implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String countryCode;
	private String countryName;
	private boolean archived;

	public Country() {
		super();
	}

	/**
	 * @param countryId
	 */
	public Country(Long id) {
		super();
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * @return the archived
	 */
	public boolean isArchived() {
		return archived;
	}

	/**
	 * @param archived
	 *            the archived to set
	 */
	public void setArchived(boolean archived) {
		this.archived = archived;
	}

}
