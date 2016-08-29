package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Permission
 *
 */
@Entity

public class Permission extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String authority;

	private Integer role;

	public Permission() {
		super();
	}
	@NotNull
	@Column(nullable = false)
	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	@NotNull
	@Column(nullable = false, length = 25)
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
