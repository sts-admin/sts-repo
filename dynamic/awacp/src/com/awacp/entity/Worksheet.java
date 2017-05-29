package com.awacp.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Worksheet
 *
 */
@Entity
@XmlRootElement
public class Worksheet extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long takeoffId; // Not null
	private Double grandTotal;
	private Set<QuoteNote> notes;
	private Set<WsManufacturerInfo> manufacturerItems;
	private String specialNotes;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	private Takeoff takeoff;

	public Worksheet() {
		super();
	}

	public Long getTakeoffId() {
		return takeoffId;
	}

	public void setTakeoffId(Long takeoffId) {
		this.takeoffId = takeoffId;
	}

	@XmlElement(name = "notes")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "WS_WORKSHEET_NOTE", joinColumns = @JoinColumn(name = "WORKSHEET"), inverseJoinColumns = @JoinColumn(name = "NOTE"))
	public Set<QuoteNote> getNotes() {
		return notes;
	}

	public void setNotes(Set<QuoteNote> notes) {
		this.notes = notes;
	}

	public String getSpecialNotes() {
		return specialNotes;
	}

	public void setSpecialNotes(String specialNotes) {
		this.specialNotes = specialNotes;
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

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	@Transient
	public Takeoff getTakeoff() {
		return takeoff;
	}

	public void setTakeoff(Takeoff takeoff) {
		this.takeoff = takeoff;
	}

	@XmlElement(name = "manufacturerItems")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "WS_MND", joinColumns = @JoinColumn(name = "WS"), inverseJoinColumns = @JoinColumn(name = "MND"))
	public Set<WsManufacturerInfo> getManufacturerItems() {
		return manufacturerItems;
	}

	public void setManufacturerItems(Set<WsManufacturerInfo> manufacturerItems) {
		this.manufacturerItems = manufacturerItems;
	}

}
