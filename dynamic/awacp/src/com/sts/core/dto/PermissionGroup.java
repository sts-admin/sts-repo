package com.sts.core.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.Permission;

@XmlRootElement
public class PermissionGroup {
	private String groupName;
	private List<Permission> permissions;

	/**
	 * 
	 */
	public PermissionGroup() {
		super();
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

}
