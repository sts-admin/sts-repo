package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Product
 *
 */
@Entity
@XmlRootElement

@NamedQueries({
	@NamedQuery(name = "Product.filterByNameMatch", query = "SELECT new com.awacp.entity.Product(s.id, s.productName) FROM Product s WHERE s.archived = 'false' AND LOWER(s.productName) LIKE :keyword"),
	@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p WHERE p.archived = 'false'")
})
public class Product extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String productName;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public Product() {
		super();
	}
	
	public Product(Long id, String productName) {
		this.setId(id);
		this.productName = productName;
	}

	@NotNull
	@Column(nullable = false, length = 255)
	public String getProductName() {
		return productName;
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
