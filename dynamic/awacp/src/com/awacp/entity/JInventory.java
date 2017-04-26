package com.awacp.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;
import com.sts.core.entity.File;

/**
 * Entity implementation class for Entity: JInventory
 *
 */
@Entity
@XmlRootElement
public class JInventory extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String item; // required
	private Integer quantity; // required
	private Integer reorderQuantity; // required
	private Double size; // required
	private Double unitPrice; // required
	private Double billableCost;

	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	private Integer minStockLevel;
	private String orderNumberWithQty;
	private List<File> images;

	private int imageCount;

	public JInventory() {
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

	public Integer getMinStockLevel() {
		return minStockLevel;
	}

	public void setMinStockLevel(Integer minStockLevel) {
		this.minStockLevel = minStockLevel;
	}

	public String getOrderNumberWithQty() {
		return orderNumberWithQty;
	}

	public void setOrderNumberWithQty(String orderNumberWithQty) {
		this.orderNumberWithQty = orderNumberWithQty;
	}

	@Transient
	public List<File> getImages() {
		return images;
	}

	public void setImages(List<File> images) {
		this.images = images;
	}

	@Transient
	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}

}
