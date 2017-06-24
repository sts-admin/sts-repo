package com.sts.core.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.Menu;
import com.sts.core.dto.PermissionGroup;
import com.sts.core.dto.StsCoreResponse;
import com.sts.core.dto.StsResponse;
import com.sts.core.dto.UserDTO;
import com.sts.core.entity.Address;
import com.sts.core.entity.Permission;
import com.sts.core.entity.User;
import com.sts.core.exception.StsCoreException;
import com.sts.core.service.UserService;
import com.sts.core.web.filter.CrossOriginFilter;

public class UserServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private UserService userService;

	@GET
	@Path("/listOnlineUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserDTO> listOnlineUsers(@Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.listUsersByOnlineStatus("online");
	}
	
	@GET
	@Path("/listOfflineUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserDTO> listOfflineUsers(@Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.listUsersByOnlineStatus("offline");
	}

	@GET
	@Path("/listAllPermissions")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Permission> listAllPermissions(@Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.listPermissions();
	}

	@GET
	@Path("/getUserMenusById")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Menu> getUserMenus(@QueryParam("userId") Long userId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.userService.getUserMenu(userId);
	}

	@GET
	@Path("/getUserMenusByNameOrEmail")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Menu> getUserMenusByNameOrEmail(@QueryParam("userNameOrEmail") String userNameOrEmail,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.getUserMenu(userNameOrEmail);
	}

	@GET
	@Path("/groupPermissionsGroup")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PermissionGroup> groupPermissionsGroup(@Context HttpServletResponse servletResponse)
			throws IOException {
		return this.userService.groupPermissionsGroup();
	}

	@GET
	@Path("/activateAccount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public StsCoreResponse activateAccount(@QueryParam("vcode") String vcode,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.activateAccount(vcode);
	}

	@POST
	@Path("/signup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public StsCoreResponse signup(User user, @Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.doSignup(user);
	}

	@GET
	@Path("/setPassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public StsCoreResponse setPassword(@QueryParam("email") String email, @QueryParam("otp") String otp,
			@QueryParam("password") String password, @Context HttpServletResponse servletResponse) throws IOException {
		StsCoreResponse result = this.userService.setPassword(email, otp, password);
		return result;
	}

	@GET
	@Path("/resetPassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public StsCoreResponse resetPassword(@QueryParam("email") String email, @QueryParam("otp") String otp,
			@QueryParam("password") String password, @Context HttpServletResponse servletResponse) throws IOException {
		StsCoreResponse result = this.userService.resetPassword(email, otp, password);
		return result;
	}

	@GET
	@Path("/checkOTP")
	@Produces(MediaType.APPLICATION_JSON)
	public String checkOTP(@QueryParam("otp") String otp, @Context HttpServletResponse servletResponse)
			throws IOException {
		boolean result = this.userService.checkOTP(otp);
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/get/user/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("id") Long id, @Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.findUser(id);
	}

	@GET
	@Path("/getUserWithPermissions/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserWithPermissions(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.userService.getUserWithPermissions(id);
	}

	@GET
	@Path("/getUserDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserDetails(@QueryParam("userNameOrEmail") String userNameOrEmail,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.getUserByUserNameOrEmail(userNameOrEmail);
	}

	@GET
	@Path("/listUser/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<User> listUser(@PathParam("pageNumber") int pageNumber, @PathParam("pageSize") int pageSize,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.listUser(pageNumber, pageSize);
	}

	@GET
	@Path("/listArchivedUser/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<User> listDeletedUser(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.listArchivedUser(pageNumber, pageSize);
	}

	@GET
	@Path("/listUsersByKeyword")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> listUser(@QueryParam("keyword") String keyword, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.userService.listUser(keyword);
	}

	@POST
	@Path("/saveUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User saveUser(User user, @Context HttpServletResponse servletResponse) throws IOException {
		User aUser = null;
		try {
			aUser = this.userService.saveUser(user);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (message.equals(StsCoreConstant.DUPLICATE_CODE.toLowerCase())) {
				code = 1000;
			} else if (e.getMessage().equals(StsCoreConstant.DUPLICATE_USERNAME.toLowerCase())) {
				code = 1001;
			} else if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = 1002;
			}
			servletResponse.sendError(code, message);

		}
		return aUser;
	}

	@POST
	@Path("/updateUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User updateUser(User user, @Context HttpServletResponse servletResponse) throws IOException {
		User aUser = null;
		try {
			aUser = this.userService.updateUser(user);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (message.equals(StsCoreConstant.DUPLICATE_CODE.toLowerCase())) {
				code = 1000;
			} else if (e.getMessage().equals(StsCoreConstant.DUPLICATE_USERNAME.toLowerCase())) {
				code = 1001;
			} else if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = 1002;
			}
			servletResponse.sendError(code, message);

		}
		return aUser;
	}

	@DELETE
	@Path("/archive/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String archiveUser(@PathParam("userId") Long userId, @Context HttpServletResponse servletResponse)
			throws IOException {
		String status = "fail";
		try {
			status = this.userService.archiveUser(userId);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (message.equals(StsCoreConstant.SINGLE_USER.toLowerCase())) {
				code = 11111;
			}
			servletResponse.sendError(code, message);
		}

		return "{\"status\":\"" + status + "\"}";
	}

	@DELETE
	@Path("/delete/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteUser(@PathParam("userId") Long userId, @Context HttpServletResponse servletResponse)
			throws IOException {
		String status = "fail";
		try {
			status = this.userService.removeUser(userId);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (message.equals(StsCoreConstant.SINGLE_USER.toLowerCase())) {
				code = 11111;
			}
			servletResponse.sendError(code, message);
		}
		return "{\"status\":\"" + status + "\"}";
	}

	@DELETE
	@Path("/activate/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String activateUser(@PathParam("userId") Long userId, @Context HttpServletResponse servletResponse)
			throws IOException {
		String status = "fail";
		try {
			status = this.userService.removeUser(userId);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (message.equals(StsCoreConstant.SINGLE_USER.toLowerCase())) {
				code = 11111;
			}
			servletResponse.sendError(code, message);
		}
		return "{\"status\":\"" + status + "\"}";
	}

	@GET
	@Path("/getUserNameAndImage/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserNameAndImage(@PathParam("userId") Long userId, @Context HttpServletResponse servletResponse)
			throws IOException {
		User user = this.userService.findUser(userId);
		return "{\"id\":\"" + userId + "\",\"name\":\"" + user.getFirstName() + " " + user.getFirstName()
				+ "\",\"src\":\"" + user.getAvtarImage() + "\"}";
	}

	@GET
	@Path("/searchUserByFilter")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserDTO> searchUserByFilter(@QueryParam("filterString") String filterString,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.searchUserByFilter(filterString);
	}

	@GET
	@Path("/filterUsersByName")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> filterUsersByName(@QueryParam("name") String name, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.userService.filterByName(name);
	}

	@GET
	@Path("/filterUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> filterUsers(@QueryParam("name") String name, @QueryParam("userId") String userId,
			@QueryParam("userCode") String userCode, @QueryParam("email") String email,
			@QueryParam("status") String status, @Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.filterUsers(name, userId, userCode, email, status);
	}

	@GET
	@Path("/getAddress")
	@Produces(MediaType.APPLICATION_JSON)
	public Address getAddress(@QueryParam("userId") Long userId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.userService.getAddress(userId);
	}

	@POST
	@Path("/saveAddress")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Address saveAddress(Address address, @Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.saveAddress(address);
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
