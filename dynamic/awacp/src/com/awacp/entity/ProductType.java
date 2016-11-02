package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: ProductType
 *
 */
@Entity
@XmlRootElement
public class ProductType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long productId;
	private String productTypeName;
	private String specModel;
	private Double range1;
	private Double range2;
	private Double range3;
	private String message;
	private Integer color;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public ProductType() {
		super();
	}

	@NotNull
	@Column(nullable = false, length = 20)
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@NotNull
	@Column(nullable = false, length = 250)
	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	@Column(length = 250)
	public String getSpecModel() {
		return specModel;
	}

	public void setSpecModel(String specModel) {
		this.specModel = specModel;
	}

	@NotNull
	@Column(nullable = false)
	public Double getRange1() {
		return range1;
	}

	public void setRange1(Double range1) {
		this.range1 = range1;
	}

	@NotNull
	@Column(nullable = false)
	public Double getRange2() {
		return range2;
	}

	public void setRange2(Double range2) {
		this.range2 = range2;
	}

	@NotNull
	@Column(nullable = false)
	public Double getRange3() {
		return range3;
	}

	public void setRange3(Double range3) {
		this.range3 = range3;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@NotNull
	@Column(nullable = false)
	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
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
