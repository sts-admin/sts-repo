package com.awacp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
public class WorksheetItem extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long worksheetInfoId; // Not null
	private Integer quantity;
	private Product product;
	private Integer list;
	private Double net;

	public WorksheetItem() {
		super();
	}

	public Long getWorksheetInfoId() {
		return worksheetInfoId;
	}

	public void setWorksheetInfoId(Long worksheetInfoId) {
		this.worksheetInfoId = worksheetInfoId;
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
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getList() {
		return list;
	}

	public void setList(Integer list) {
		this.list = list;
	}

	public Double getNet() {
		return net;
	}

	public void setNet(Double net) {
		this.net = net;
	}

}
