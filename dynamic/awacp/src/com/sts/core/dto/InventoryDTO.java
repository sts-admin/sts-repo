package com.sts.core.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InventoryDTO {
	private Long id;
	private String item; // required
	private Integer quantity; // required
	private Double size; // required

	public InventoryDTO(Long id, String item, Integer quantity, Double size) {
		super();
		this.id = id;
		this.item = item;
		this.quantity = quantity;
		this.size = size;
	}

	/**
	 * 
	 */
	public InventoryDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

}
