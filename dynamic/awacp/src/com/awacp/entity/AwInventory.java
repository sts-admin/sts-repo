package com.awacp.entity;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: AwfInventory
 *
 */
@Entity
@XmlRootElement
public class AwInventory extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String item; // required
	private Integer quantity; // required
	private Integer reorderQuantity; // required
	private Double size; // required
	private Double unitPrice; // required
	private Double billableCost;

	public AwInventory() {
		super();
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getReorderQuantity() {
		return reorderQuantity;
	}

	public void setReorderQuantity(Integer reorderQuantity) {
		this.reorderQuantity = reorderQuantity;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getBillableCost() {
		return billableCost;
	}

	public void setBillableCost(Double billableCost) {
		this.billableCost = billableCost;
	}

}
