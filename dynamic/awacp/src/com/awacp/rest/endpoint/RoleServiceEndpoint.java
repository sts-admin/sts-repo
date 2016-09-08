package com.awacp.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.awacp.service.RoleService;
import com.sts.core.dto.PermissionGroup;
import com.sts.core.entity.Permission;
import com.sts.core.entity.Role;
import com.sts.core.web.filter.CrossOriginFilter;

public class RoleServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private RoleService roleService;

	@GET
	@Path("/listRoles")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Role> listRoles(@Context HttpServletResponse servletResponse) throws IOException {
		return this.roleService.listRoles();
	}
	@GET
	@Path("/listAllPermissions")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Permission> listAllPermissions(@Context HttpServletResponse servletResponse) throws IOException {
		return this.roleService.listPermissions();
	}

	@GET
	@Path("/getRole")
	@Produces(MediaType.APPLICATION_JSON)
	public Role getRole(@QueryParam("roleName") String roleName, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.roleService.getRole(roleName);
	}

	@GET
	@Path("/getRoleWithPermissions")
	@Produces(MediaType.APPLICATION_JSON)
	public Role getRoleWithPermissions(@QueryParam("roleName") String roleName,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.roleService.getRoleWithPermissions(roleName);
	}
	@GET
	@Path("/groupPermissionsByGroup")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PermissionGroup> groupPermissionsByCategory(@QueryParam("roleName") String roleName,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.roleService.groupPermissionsByGroup(roleName);
	}

	@POST
	@Path("/saveRole")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Role saveRole(Role role, @Context HttpServletResponse servletResponse) throws IOException {
		return this.roleService.saveRole(role);
	}

	@POST
	@Path("/updateRole")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Role updateRole(Role role, @Context HttpServletResponse servletResponse) throws IOException {
		return this.roleService.updateRole(role);
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

}
