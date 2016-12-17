package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: ShipTo
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ShipTo.filterByAddressMatch", query = "SELECT new com.awacp.entity.ShipTo(s.id, s.shipToAddress) FROM ShipTo s WHERE s.archived = 'false' AND LOWER(s.shipToAddress) LIKE :keyword")

})
public class ShipTo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String shipToAddress;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public ShipTo() {
		super();
	}

	public ShipTo(Long id, String detail) {
		this.setId(id);
		this.shipToAddress = detail;
	}

	@NotNull
	@Column(nullable = false, length = 250)
	public String getShipToAddress() {
		return shipToAddress;
	}

	public void setShipToAddress(String shipToAddress) {
		this.shipToAddress = shipToAddress;
	}
	
	@NotNull
	@Column(nullable = false, length = 10)
	public String getCreatedByUserCode() {
		return createdByUserCode;
	}

	

	public void setCreatedByUserCode(String createdByUserCode) {
		this.createdByUserCode = createdByUserCode;
	}

	@Column(nullable = true, length = 10)
	public String getUpdatedByUserCode() {
		return updatedByUserCode;
	}

	public void setUpdatedByUserCode(String updatedByUserCode) {
		this.updatedByUserCode = updatedByUserCode;
	}

}
