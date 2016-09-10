package com.sts.core.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: Permission
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Permission.listAll", query = "SELECT p FROM Permission p WHERE p.archived = 'false'"),
		@NamedQuery(name = "Permission.getByName", query = "SELECT p FROM Permission p WHERE p.archived = 'false' AND p.authority =:permissionName"),
		@NamedQuery(name = "Permission.getAllMatchingPermissions", query = "SELECT p FROM Permission p WHERE p.archived = 'false' AND LOWER( p.authority ) LIKE :exp")
		})
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
	private String label;

	public Permission() {
		super();
	}

	public Permission(String authority, String description) {
		super();
		this.authority = authority;
		this.description = description;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
