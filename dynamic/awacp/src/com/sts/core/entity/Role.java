package com.sts.core.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: Role
 *
 */
@Entity
@XmlRootElement
@NamedQueries({ 
	@NamedQuery(name = "Role.listAll", query = "SELECT r FROM Role r WHERE r.archived = 'false'"),
	@NamedQuery(name = "Role.getByName", query = "SELECT r FROM Role r WHERE r.archived = 'false' AND r.roleName =:roleName") 
})
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;
	private String roleName;
	private Calendar dateCreated;
	private Calendar dateUpdated;
	private Long createdById;
	private Long updatedById;
	private Long version;
	private boolean archived;
	private String roleDescription;

	private Set<Permission> permissions;

	// Transient attributes
	private String[] permissionArray;

	public Role() {
		super();
	}

	@Id
	@Column(nullable = false, length = 20)
	@NotNull
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	@XmlElement(name = "permissions")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "ROLE_PERMISSION", joinColumns = @JoinColumn(name = "ROLEID") , inverseJoinColumns = @JoinColumn(name = "PERMISSIONID") )
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@NotNull
	@Column(nullable = false, length = 40)
	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	@Transient
	public String[] getPermissionArray() {
		return permissionArray;
	}

	public void setPermissionArray(String[] permissionArray) {
		this.permissionArray = permissionArray;
	}

}
