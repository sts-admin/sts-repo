package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Pdni
 *
 */
@Entity
@XmlRootElement

@NamedQueries({
	@NamedQuery(name = "Pdni.findAll", query = "SELECT p FROM Pdni p WHERE p.archived = 'false'")
})
public class Pdni extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String pdniName;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	public Pdni() {
		super();
	}

	@NotNull
	@Column(nullable = false, length = 100)
	public String getPdniName() {
		return pdniName;
	}

	public void setPdniName(String pdniName) {
		this.pdniName = pdniName;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pdni other = (Pdni) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (getId().intValue() != other.getId().intValue())
			return false;
		return true;
	}

}
