package com.awacp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: WsProductInfo
 *
 */
@Entity
@XmlRootElement
public class WsManufacturerInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long worksheetId; // Not null
	private MnD manufacturer;
	private Double listTotal;
	private Double netTotal;
	private Double quoteAmount;
	private Double multiplier;
	private Double multPercentAmount;
	private Double freight;
	private Double totalAmount;
	private Double percent;
	private List<Pdni> pdnis;
	private List<WsProductInfo> productItems;
	private String comments;

	public WsManufacturerInfo() {
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

	@Transient
	public List<Pdni> getPdnis() {
		return pdnis;
	}

	public void setPdnis(List<Pdni> pdnis) {
		this.pdnis = pdnis;
	}

	@Lob
	@Column(length = 1024)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Double getQuoteAmount() {
		return quoteAmount;
	}

	public void setQuoteAmount(Double quoteAmount) {
		this.quoteAmount = quoteAmount;
	}

	public Double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Double multiplier) {
		this.multiplier = multiplier;
	}

	public Double getMultPercentAmount() {
		return multPercentAmount;
	}

	public void setMultPercentAmount(Double multPercentAmount) {
		this.multPercentAmount = multPercentAmount;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}
	
	@Transient
	public List<WsProductInfo> getProductItems() {
		return productItems;
	}

	public void setProductItems(List<WsProductInfo> productItems) {
		this.productItems = productItems;
	}

}
