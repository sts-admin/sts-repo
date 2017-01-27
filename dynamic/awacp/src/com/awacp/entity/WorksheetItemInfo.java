package com.awacp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: WorksheetItem
 *
 */
@Entity
@XmlRootElement
public class WorksheetItemInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long worksheetId; // Not null
	private MnD manufacturer;
	private Double listTotal;
	private Double netTotal;
	private Double tlp;
	private Double mult;
	private Double netAmount;
	private Double frt;
	private Double ttl;
	private List<Pdni> pdnis;
	private List<WorksheetItem> productLineItems;
	private String comments;

	public WorksheetItemInfo() {
		super();
	}

	public Long getWorksheetId() {
		return worksheetId;
	}

	public void setWorksheetId(Long worksheetId) {
		this.worksheetId = worksheetId;
	}

	@XmlElement(name = "manufacturer")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "MNDID", unique = false, nullable = false, updatable = true)
	public MnD getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(MnD manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Double getListTotal() {
		return listTotal;
	}

	public void setListTotal(Double listTotal) {
		this.listTotal = listTotal;
	}

	public Double getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(Double netTotal) {
		this.netTotal = netTotal;
	}

	public Double getTlp() {
		return tlp;
	}

	public void setTlp(Double tlp) {
		this.tlp = tlp;
	}

	public Double getMult() {
		return mult;
	}

	public void setMult(Double mult) {
		this.mult = mult;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getFrt() {
		return frt;
	}

	public void setFrt(Double frt) {
		this.frt = frt;
	}

	public Double getTtl() {
		return ttl;
	}

	public void setTtl(Double ttl) {
		this.ttl = ttl;
	}

	@XmlElement(name = "pdnis")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "WSINFO_PDNI", joinColumns = @JoinColumn(name = "WSINFO") , inverseJoinColumns = @JoinColumn(name = "PDNI") )
	public List<Pdni> getPdnis() {
		return pdnis;
	}

	public void setPdnis(List<Pdni> pdnis) {
		this.pdnis = pdnis;
	}

	@XmlElement(name = "productLineItems")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "WSINFO_PRODITEM", joinColumns = @JoinColumn(name = "WSINFO") , inverseJoinColumns = @JoinColumn(name = "PRODITEM") )
	public List<WorksheetItem> getProductLineItems() {
		return productLineItems;
	}

	public void setProductLineItems(List<WorksheetItem> productLineItems) {
		this.productLineItems = productLineItems;
	}

	@Lob
	@Column(length = 1024)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
