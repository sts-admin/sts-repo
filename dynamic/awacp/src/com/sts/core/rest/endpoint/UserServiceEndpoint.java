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

import com.sts.core.dto.StsCoreResponse;
import com.sts.core.dto.UserDTO;
import com.sts.core.entity.Address;
import com.sts.core.entity.User;
import com.sts.core.service.UserService;
import com.sts.core.web.filter.CrossOriginFilter;

public class UserServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private UserService userService;

	@GET
	@Path("/activateAccount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public StsCoreResponse activateAccount(@QueryParam("vcode") String vcode, @Context HttpServletResponse servletResponse) throws IOException {
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
	public StsCoreResponse setPassword(@QueryParam("email") String email, @QueryParam("otp") String otp, @QueryParam("password") String password, @Context HttpServletResponse servletResponse) throws IOException {
		StsCoreResponse result = this.userService.setPassword(email, otp, password);
		return result;
	}


	@GET
	@Path("/resetPassword")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public StsCoreResponse resetPassword(@QueryParam("email") String email, @QueryParam("otp") String otp, @QueryParam("password") String password, @Context HttpServletResponse servletResponse) throws IOException {
		StsCoreResponse result = this.userService.resetPassword(email, otp, password);
		return result;
	}

	@GET
	@Path("/checkOTP")
	@Produces(MediaType.APPLICATION_JSON)
	public String checkOTP(@QueryParam("otp") String otp, @Context HttpServletResponse servletResponse) throws IOException {
		boolean result = this.userService.checkOTP(otp);
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/get/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("userId") Long userId, @Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.findUser(userId);
	}

	@GET
	@Path("/getUserDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserDetails(@QueryParam("userNameOrEmail") String userNameOrEmail, @Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.getUserByUserNameOrEmail(userNameOrEmail);
	}

	

	@GET
	@Path("/listUser")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> listUser(@Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.listUser();
	}
	
	@GET
	@Path("/listUsersByKeyword")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> listUser(@QueryParam("keyword") String keyword, @Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.listUser(keyword);
	}

	@POST
	@Path("/saveUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public User saveUser(User user, @Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.saveUser(user);
	}

	@DELETE
	@Path("/delete/user/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteUser(@PathParam("userId") Long userId, @Context HttpServletResponse servletResponse) throws IOException {
		this.userService.removeUser(userId);
		return "{\"Id\":\"" + userId + "\"}";
	}

	@GET
	@Path("/getUserNameAndImage/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserNameAndImage(@PathParam("userId") Long userId, @Context HttpServletResponse servletResponse) throws IOException {
		User user = this.userService.findUser(userId);
		return "{\"id\":\"" + userId + "\",\"name\":\"" + user.getFirstName() + " " + user.getFirstName() + "\",\"src\":\"" + user.getAvtarImage() + "\"}";
	}

	@GET
	@Path("/searchUserByFilter")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserDTO> searchUserByFilter(@QueryParam("filterString") String filterString, @Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.searchUserByFilter(filterString);
	}
	
	@GET
	@Path("/getAddress")
	@Produces(MediaType.APPLICATION_JSON)
	public Address getAddress(@QueryParam("userId") Long userId, @Context HttpServletResponse servletResponse) throws IOException {
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
