package com.sts.core.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Permission
 *
 */
@Entity
@XmlRootElement
public class Permission {

	private static final long serialVersionUID = 1L;
	private String authority;
	private String description;
	private Calendar dateCreated;
	private Calendar dateUpdated;
	private Long createdById;
	private Long updatedById;
	private Long version;
	private boolean archived;

	public Permission() {
		super();
	}

	@Id
	@NotNull
	@Column(nullable = false, length = 30)
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Calendar getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Calendar dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Calendar getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Calendar dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public Long getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Long updatedById) {
		this.updatedById = updatedById;
	}

	@Version
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	@PrePersist
	public void createAuditInfo() {
		setDateCreated(Calendar.getInstance());
	}

	@PreUpdate
	public void updateAuditInfo() {
		setDateUpdated(Calendar.getInstance());
	}

	@NotNull
	@Column(nullable = false, length = 35)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
