package com.awacp.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.service.RoleService;
import com.sts.core.dto.Menu;
import com.sts.core.dto.MenuItem;
import com.sts.core.dto.PermissionGroup;
import com.sts.core.entity.Permission;
import com.sts.core.entity.Role;
import com.sts.core.entity.User;
import com.sts.core.service.UserService;

public class RoleServiceImpl implements RoleService {
	private EntityManager entityManager;

	@Autowired
	UserService userService;

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
		Role existingRole = getRole(role.getRoleName());
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
		} else {
			existingRole.getPermissions().clear();
		}
		System.err.println("role permission size = " + existingRole.getPermissions().size());
		getEntityManager().merge(existingRole);
		getEntityManager().flush();
		return existingRole;
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
			System.err.println("uniquePermissions size = "+ uniquePermissions.size());
			for (Object[] permission : uniquePermissions) {
				PermissionGroup pg = new PermissionGroup();
				pg.setGroupName(permission[1].toString());
				String keyword = permission[0].toString().split("_")[0];
				pg.setPermissions(getAllMatchingPermissions(keyword));
				groups.add(pg);
				System.err.println("Group Name = "+ permission[0].toString() + " permission size = "+ pg.getPermissions().size());
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

	@Override
	public List<Menu> getUserMenu(String userNameOrEmail) {
		User user = userService.getUserByUserNameOrEmail(userNameOrEmail);
		Role role = getRole(user.getRole().getRoleName());
		if (role != null) {
			role.getPermissions();
		}
		return getMenuOfRole(role);
	}

	@Override
	public List<Menu> getUserMenu(Long userId) {

		User user = getEntityManager().find(User.class, userId);
		Role role = getRole(user.getRole().getRoleName());
		if (role != null) {
			role.getPermissions();
		}

		return getMenuOfRole(role);
	}

	private List<Menu> getMenuOfRole(Role role) {
		if (role.getPermissions() != null) {
			System.err.println("role permissions = " + role.getPermissions().size());
		}

		List<Menu> menus = new ArrayList<Menu>();
		List<MenuItem> items = null;
		Menu aMenu = null;
		for (PermissionGroup group : groupPermissionsGroup()) {
			aMenu = new Menu(group.getGroupName(), group.getGroupName());
			if (group.getPermissions() != null && !group.getPermissions().isEmpty()) {
				group.getPermissions().retainAll(role.getPermissions());
				if (group.getPermissions() == null || group.getPermissions().isEmpty()) {
					continue;
				}
				items = new ArrayList<MenuItem>();
				int index = 0;
				int size = group.getPermissions().size();
				boolean emptyUrl = false, bbt = false;
				for (Permission permission : group.getPermissions()) {
					emptyUrl = bbt = false;
					if (permission.getUrl() == null || permission.getUrl().isEmpty()) {
						emptyUrl = true;
					}
					if (permission.getAuthority().contains("bbt")) {
						bbt = true;
						items = getBbtItems(items);
					}
					if (index != 0 && index != (size - 1) && !emptyUrl && !bbt) {
						items.add(new MenuItem("divider", "#"));
					}
					if (emptyUrl) {
						continue;
					}
					if (bbt) {
						break;
					}
					items.add(new MenuItem(permission.getDescription(), permission.getUrl()));
					index++;
				}
				aMenu.setItems(items);
			}
			if (items != null && !items.isEmpty()) {
				menus.add(aMenu);
			}
		}
		System.err.println("menus size = " + menus.size());
		return menus;

	}

	private List<MenuItem> getBbtItems(List<MenuItem> items) {
		items.add(new MenuItem("Manage Engineer", "engineers"));
		items.add(new MenuItem("divider", "#"));
		items.add(new MenuItem("Manage Architect", "architects"));
		items.add(new MenuItem("divider", "#"));
		items.add(new MenuItem("Manage Contractor", "contractors"));
		items.add(new MenuItem("divider", "#"));
		items.add(new MenuItem("Manage Bidder", "bidders"));
		items.add(new MenuItem("divider", "#"));
		items.add(new MenuItem("Manage Trucker", "truckers"));
		items.add(new MenuItem("divider", "#"));

		items.add(new MenuItem("Manage Specification", "specifications"));
		items.add(new MenuItem("divider", "#"));

		items.add(new MenuItem("Manage Product", "products"));
		items.add(new MenuItem("divider", "#"));
		
		items.add(new MenuItem("Manage General Contractors", "gcs"));
		items.add(new MenuItem("divider", "#"));

		items.add(new MenuItem("Manage Ship To", "ships"));
		items.add(new MenuItem("divider", "#"));

		items.add(new MenuItem("Manage PDNI", "pndis"));
		items.add(new MenuItem("divider", "#"));

		items.add(new MenuItem("Manage Quote Notes", "qnotes"));
		items.add(new MenuItem("divider", "#"));

		items.add(new MenuItem("Manage Manufacture & Description", "manufactures"));
		items.add(new MenuItem("divider", "#"));

		items.add(new MenuItem("Manage Item Shipped", "iships"));
		items.add(new MenuItem("divider", "#"));

		items.add(new MenuItem("Manage Shipped Via", "vships"));
		items.add(new MenuItem("divider", "#"));

		items.add(new MenuItem("Delete File", "deletefiles"));
		return items;
	}

}
