package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
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
		@NamedQuery(name = "GeneralContractor.listAll", query = "SELECT gc FROM GeneralContractor gc WHERE gc.archived = 'false'"),
		@NamedQuery(name = "GeneralContractor.countAll", query = "SELECT COUNT(gc.id) FROM GeneralContractor gc WHERE gc.archived = 'false'"),
		@NamedQuery(name = "GeneralContractor.getByEmail", query = "SELECT gc FROM GeneralContractor gc WHERE gc.archived = 'false' AND LOWER(gc.email) = :email") })
public class GeneralContractor extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String name;
	private Long salesPerson; // ID of a AWACP System User
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private String phoneOffice;
	private String fax;
	private String email;
	private String website;
	private String comment;
	private String basicSpec;
	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	// Transient
	private String salesPersonName;

	public GeneralContractor() {
		super();
	}

	@NotNull
	@Column(nullable = false)
	public Long getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(Long salesPerson) {
		this.salesPerson = salesPerson;
	}

	@Column(length = 150)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(length = 50)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(length = 20)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(length = 20)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@NotNull
	@Column(nullable = false, length = 100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(length = 150)
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Column(length = 200)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Transient
	public String getSalesPersonName() {
		return salesPersonName;
	}

	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
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

	@Column(length = 10)
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Column(length = 150)
	public String getBasicSpec() {
		return basicSpec;
	}

	public void setBasicSpec(String basicSpec) {
		this.basicSpec = basicSpec;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(length = 50)
	public String getState() {
		return state;
	}

	@Column(nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 20)
	public String getPhoneOffice() {
		return phoneOffice;
	}

	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice;
	}

	@PrePersist
	public void setDefaults() {
		if (this.getAddress() == null) {
			this.address = "";
		}
		if (this.getBasicSpec() == null) {
			this.basicSpec = "";
		}
		if (this.getCity() == null) {
			this.city = "";
		}
		if (this.getState() == null) {
			this.state = "";
		}
		if (this.getComment() == null) {
			this.comment = "";
		}
		if (this.getFax() == null) {
			this.fax = "";
		}
		if (this.getUpdatedByUserCode() == null) {
			this.updatedByUserCode = "";
		}
		if (this.getPhone() == null) {
			this.phone = "";
		}
		if (this.getPhoneOffice() == null) {
			this.phoneOffice = "";
		}
		if (this.getZip() == null) {
			this.fax = "";
		}
	}
}
