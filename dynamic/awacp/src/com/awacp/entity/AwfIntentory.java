package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: AwfIntentory
 *
 */
@Entity
@XmlRootElement
public class AwfIntentory extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String itemName; // length 250
	private Integer quantity; // length 4
	private String sizes; // length 250
	private String orderNumber; // length 100
	private Integer reorderQty; // length 4
	private Integer orderQty; // length 4
	private Double unitPrice; // 18, 2
	private Double billableCost; // 18, 2 not null

	public AwfIntentory() {
		super();
	}

	@NotNull
	@Column(nullable = false, length = 250)
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@NotNull
	@Column(nullable = false)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSizes() {
		return sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getReorderQty() {
		return reorderQty;
	}

	public void setReorderQty(Integer reorderQty) {
		this.reorderQty = reorderQty;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	@NotNull
	@Column(columnDefinition = "Decimal(10,2) default '00.00'")
	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@NotNull
	@Column(columnDefinition = "Decimal(10,2) default '00.00'")
	public Double getBillableCost() {
		return billableCost;
	}

	public void setBillableCost(Double billableCost) {
		this.billableCost = billableCost;
	}

}
