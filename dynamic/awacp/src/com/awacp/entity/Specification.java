package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Address
 *
 */
@Entity
@XmlRootElement
public class Specification extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String specification;
	private Long salesPerson;

	public Specification() {
		super();
	}

	@NotNull
	@Column(nullable = false, length = 200)
	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	@NotNull
	@Column(nullable = false)
	public Long getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(Long salesPerson) {
		this.salesPerson = salesPerson;
	}

}
