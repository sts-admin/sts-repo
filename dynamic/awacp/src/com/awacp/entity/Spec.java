package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Spec
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Spec.filterByDetailMatch", query = "SELECT new com.awacp.entity.Spec(s.id, s.detail) FROM Spec s WHERE s.archived = 'false' AND LOWER(s.detail) LIKE :keyword")

})
public class Spec extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String detail;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public Spec() {
		super();
	}

	public Spec(Long id, String detail) {
		this.setId(id);
		this.detail = detail;
	}

	@NotNull
	@Column(nullable = false, length = 250)
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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
