package com.awacp.entity;

import java.util.List;

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
	private List<QuoteNote> notes;
	private List<WsManufacturerInfo> manufacturerItems;
	private String specialNotes;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	// Transient

	private Takeoff takeoff;
	private String[] manufacturerArray;
	private String[] quoteArray;

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
	@ManyToMany(cascade = {CascadeType.DETACH })
	@JoinTable(name = "WS_NOTE", joinColumns = @JoinColumn(name = "WS") , inverseJoinColumns = @JoinColumn(name = "NOTE") )
	public List<QuoteNote> getNotes() {
		return notes;
	}

	public void setNotes(List<QuoteNote> notes) {
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
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "WS_MND", joinColumns = @JoinColumn(name = "WS") , inverseJoinColumns = @JoinColumn(name = "MND") )
	public List<WsManufacturerInfo> getManufacturerItems() {
		return manufacturerItems;
	}

	public void setManufacturerItems(List<WsManufacturerInfo> manufacturerItems) {
		this.manufacturerItems = manufacturerItems;
	}

	@Transient
	public String[] getManufacturerArray() {
		return manufacturerArray;
	}

	public void setManufacturerArray(String[] manufacturerArray) {
		this.manufacturerArray = manufacturerArray;
	}

	@Transient
	public String[] getQuoteArray() {
		return quoteArray;
	}

	public void setQuoteArray(String[] quoteArray) {
		this.quoteArray = quoteArray;
	}

}
