package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: ShippedVia
 *
 */
@Entity
@XmlRootElement

@NamedQueries({
	@NamedQuery(name = "ShippedVia.findAll", query = "SELECT p FROM ShippedVia p WHERE p.archived = 'false'")

})
public class ShippedVia extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String shippedViaAddress;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public ShippedVia() {
		super();
	}
	
	public ShippedVia(Long id, String shippedViaAddress) {
		this.setId(id);
		this.shippedViaAddress = shippedViaAddress;
	}

	@NotNull
	@Column(nullable = false, length = 100)
	public String getShippedViaAddress() {
		return shippedViaAddress;
	}

	public void setShippedViaAddress(String productName) {
		this.shippedViaAddress = productName;
	}

	@NotNull
	@Column(nullable = false, length = 10)
	public String getCreatedByUserCode() {
		return createdByUserCode;
	}

	public void setCreatedByUserCode(String createdByUserCode) {
		this.createdByUserCode = createdByUserCode;
	}

	@Column(length = 10)
	public String getUpdatedByUserCode() {
		return updatedByUserCode;
	}

	public void setUpdatedByUserCode(String updatedByUserCode) {
		this.updatedByUserCode = updatedByUserCode;
	}

}
