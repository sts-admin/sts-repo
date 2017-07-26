package com.awacp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
public class WsProductInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer quantity;
	private MnDType product;
	private Double listAmount;
	private Double netAmount;

	public WsProductInfo() {
		super();
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@XmlElement(name = "product")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "PRODUCTID", unique = false, nullable = false, updatable = true)
	public MnDType getProduct() {
		return product;
	}

	public void setProduct(MnDType product) {
		this.product = product;
	}

	public Double getListAmount() {
		return listAmount;
	}

	public void setListAmount(Double listAmount) {
		this.listAmount = listAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
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
		WsProductInfo other = (WsProductInfo) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (getId().intValue() != other.getId().intValue())
			return false;
		return true;
	}
}
