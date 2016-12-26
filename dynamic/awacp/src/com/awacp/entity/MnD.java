package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: MnD
 *
 */
@Entity
@XmlRootElement

@NamedQueries({ @NamedQuery(name = "MnD.findAll", query = "SELECT p FROM MnD p WHERE p.archived = 'false'") })
public class MnD extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String productName;
	private String type;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public MnD() {
		super();
	}

	@NotNull
	@Column(nullable = false, length = 100)
	public String getProductName() {
		return productName;
	}

	public void setProduct(String productName) {
		this.productName = productName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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
