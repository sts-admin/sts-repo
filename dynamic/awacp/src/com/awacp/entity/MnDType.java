package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: MnDType
 *
 */
@Entity
@XmlRootElement

@NamedQueries({ 
		@NamedQuery(name = "MnDType.findAll", query = "SELECT p FROM MnDType p WHERE p.archived = 'false'"),
		@NamedQuery(name = "MnDType.findAllByMndId", query = "SELECT p FROM MnDType p WHERE p.archived = 'false' AND p.mndId = :mndId") 
})
public class MnDType extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long mndId;
	private String productName;
	private String model;
	private String rangeOne;
	private String rangeTwo;
	private String rangeThree;
	private String type;
	private String message;
	private boolean flag;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public MnDType() {
		super();
	}
	
	public MnDType(Long mndId, String productName) {
		this.mndId = mndId;
		this.productName = productName;
	}
	public Long getMndId() {
		return mndId;
	}

	public void setMndId(Long mndId) {
		this.mndId = mndId;
	}

	@NotNull
	@Column(nullable = false, length = 255)
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRangeOne() {
		return rangeOne;
	}

	public void setRangeOne(String rangeOne) {
		this.rangeOne = rangeOne;
	}

	public String getRangeTwo() {
		return rangeTwo;
	}

	public void setRangeTwo(String rangeTwo) {
		this.rangeTwo = rangeTwo;
	}

	public String getRangeThree() {
		return rangeThree;
	}

	public void setRangeThree(String rangeThree) {
		this.rangeThree = rangeThree;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
