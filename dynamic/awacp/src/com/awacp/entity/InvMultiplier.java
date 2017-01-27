package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: InvMultiplier
 *
 */
@Entity
@XmlRootElement

@NamedQueries({
	@NamedQuery(name = "InvMultiplier.findByInvName", query = "SELECT new com.awacp.entity.InvMultiplier(s.id, s.inventoryName) FROM InvMultiplier s WHERE s.archived = 'false' AND LOWER(s.inventoryName) LIKE :keyword"),
	@NamedQuery(name = "InvMultiplier.findAll", query = "SELECT p FROM InvMultiplier p WHERE p.archived = 'false'")
})
public class InvMultiplier extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String inventoryName;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public InvMultiplier() {
		super();
	}
	
	public InvMultiplier(Long id, String inventoryName) {
		this.setId(id);
		this.inventoryName = inventoryName;
	}

	@NotNull
	@Column(nullable = false, 
	length = 100)
	public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
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
