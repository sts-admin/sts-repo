package com.sts.core.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.ReadOnly;

/**
 * Entity implementation class for Entity: State
 *
 */
@Entity
@ReadOnly
@NamedQueries({ 
	@NamedQuery(name = "State.findAll", query = "SELECT s FROM State s"), 
	@NamedQuery(name = "State.findByCountryId", query = "SELECT s FROM State s WHERE s.country.id = :countryId") 
})
@XmlRootElement
public class State implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String stateCode;
	private String stateName;
	private Country country;
	private boolean archived;

	public State() {
		super();
	}

	/**
	 * @param stateId
	 */
	public State(Long id) {
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
	@NotNull
	@Column(nullable = false)
	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	@NotNull
	@Column(nullable = false)
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@XmlElement(name = "country")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "COUNTRYID", unique = false, nullable = false, updatable = false)
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
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
