package com.awacp.entity;

import java.util.Set;

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
 * Entity implementation class for Entity: WsProductInfo
 *
 */
@Entity
@XmlRootElement
public class WsManufacturerInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private MnD manufacturer;
	private Double listTotal;
	private Double netTotal;
	private Double quoteAmount;
	private Double multiplier;
	private Double multPercentAmount;
	private Double freight;
	private Double totalAmount;
	private Double percent;
	private Set<Pdni> pdnis;
	private Set<WsProductInfo> productItems;
	private String comments;

	public WsManufacturerInfo() {
		super();
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

	@XmlElement(name = "pdnis")
	@ManyToMany(cascade = { CascadeType.DETACH })
	@JoinTable(name = "WS_MND_PDNI", joinColumns = @JoinColumn(name = "MND") , inverseJoinColumns = @JoinColumn(name = "PDNI") )
	public Set<Pdni> getPdnis() {
		return pdnis;
	}

	public void setPdnis(Set<Pdni> pdnis) {
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

	@XmlElement(name = "productItems")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "WS_MND_PRODUCT", joinColumns = @JoinColumn(name = "MND") , inverseJoinColumns = @JoinColumn(name = "PRODUCT") )
	public Set<WsProductInfo> getProductItems() {
		return productItems;
	}

	public void setProductItems(Set<WsProductInfo> productItems) {
		this.productItems = productItems;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WsManufacturerInfo other = (WsManufacturerInfo) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (getId().intValue() != other.getId().intValue())
			return false;
		return true;
	}
}
