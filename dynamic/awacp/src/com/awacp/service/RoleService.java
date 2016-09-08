package com.awacp.service;

import java.util.List;

import com.sts.core.dto.PermissionGroup;
import com.sts.core.entity.Permission;
import com.sts.core.entity.Role;

public interface RoleService {
	public List<Permission> listPermissions();
	
	public List<PermissionGroup> groupPermissionsGroup();
	
	public Permission getPermission(String permissionName);

	public Role saveRole(Role role);

	public Role updateRole(Role role);

	public Role getRole(String roleName);
	
	public Role getRoleWithPermissions(String roleName);

	public List<Role> listRoles();

	public List<Role> listRolesWithPermissions();
	
}
