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
	@NamedQuery(name = "ItemShipped.findAll", query = "SELECT p FROM ItemShipped p WHERE p.archived = 'false'")

})
public class ItemShipped extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String item;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public ItemShipped() {
		super();
	}

	@NotNull
	@Column(nullable = false, length = 250)
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
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
