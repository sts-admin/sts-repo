package com.awacp.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.service.RoleService;
import com.sts.core.dto.PermissionGroup;
import com.sts.core.entity.Permission;
import com.sts.core.entity.Role;

public class RoleServiceImpl implements RoleService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> listPermissions() {
		return getEntityManager().createNamedQuery("Permission.listAll").getResultList();
	}

	@Override
	@Transactional
	public Role saveRole(Role role) {
		getEntityManager().persist(role);
		getEntityManager().flush();
		return role;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> listRoles() {
		return getEntityManager().createNamedQuery("Role.listAll").getResultList();
	}

	@Override
	@Transactional
	public Role updateRole(Role role) {
		System.err.println("Role name = "+ role);
		Role existingRole = getRoleWithPermissions(role.getRoleName());
		String[] permissionArray = role.getPermissionArray();
		if (permissionArray != null && permissionArray.length > 0) {
			Set<Permission> permissions = new HashSet<Permission>();
			for (String permissionName : permissionArray) {
				Permission permission = getPermission(permissionName);
				if (permission != null) {
					permissions.add(permission);
				}
			}
			existingRole.setPermissions(permissions);
		} else if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
			existingRole.getPermissions().clear();
		}
		role = getEntityManager().merge(existingRole);
		getEntityManager().flush();
		return role;
	}

	@Override
	public List<Role> listRolesWithPermissions() {
		List<Role> roles = listRoles();
		if (roles != null && !roles.isEmpty()) {
			for (Role role : roles) {
				role.getPermissions(); // Fetch permissions.
			}
		}
		return roles;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Role getRole(String roleName) {
		System.err.println("getRole roleName = "+ roleName);
		List<Role> roles = getEntityManager().createNamedQuery("Role.getByName").setParameter("roleName", roleName)
				.getResultList();
		return roles == null || roles.isEmpty() ? null : roles.get(0);
	}

	@Override
	public Role getRoleWithPermissions(String roleName) {
		Role role = getRole(roleName);
		if (role != null) {
			role.getPermissions();
		}
		if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
			String[] permissions = new String[role.getPermissions().size()];
			int i = 0;
			for (Permission permission : role.getPermissions()) {
				permissions[i++] = permission.getAuthority();
			}
			role.setPermissionArray(permissions);
		}
		return role;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Permission getPermission(String permissionName) {
		List<Permission> permissions = getEntityManager().createNamedQuery("Permission.getByName")
				.setParameter("permissionName", permissionName).getResultList();
		return permissions == null || permissions.isEmpty() ? null : permissions.get(0);
	}

	@Override
	public List<PermissionGroup> groupPermissionsGroup() {
		List<Object[]> uniquePermissions = getUniquePermissionGroups();
		List<PermissionGroup> groups = null;
		if (uniquePermissions != null && !uniquePermissions.isEmpty()) {
			groups = new ArrayList<PermissionGroup>();
			for (Object[] permission : uniquePermissions) {
				PermissionGroup pg = new PermissionGroup();
				pg.setGroupName(permission[1].toString());
				String keyword = permission[0].toString().split("_")[0];
				pg.setPermissions(getAllMatchingPermissions(keyword));
				groups.add(pg);
			}
		}

		return groups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getUniquePermissionGroups() {
		String query = "SELECT DISTINCT(SUBSTRING_INDEX(AUTHORITY,'_',1)) AS authority, label FROM PERMISSION";
		return getEntityManager().createNativeQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getAllMatchingPermissions(String keyword) {
		return getEntityManager().createNamedQuery("Permission.getAllMatchingPermissions")
				.setParameter("exp", keyword.toLowerCase() + "\\_%").getResultList();
	}
}
