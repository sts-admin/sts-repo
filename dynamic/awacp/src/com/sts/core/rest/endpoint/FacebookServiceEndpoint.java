package com.sts.core.rest.endpoint;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.sts.core.dto.StsCoreResponse;
import com.sts.core.entity.RoleType;
import com.sts.core.service.FacebookService;
import com.sts.core.web.filter.CrossOriginFilter;

public class FacebookServiceEndpoint extends CrossOriginFilter {
	@Autowired
	private FacebookService facebookService;

	@POST
	@Path("/setupFacebookProfile")
	@Produces(MediaType.APPLICATION_JSON)
	public StsCoreResponse setupFacebookProfile(@QueryParam("accessToken") String accessToken,
			@Context HttpServletResponse servletResponse) throws IOException {
		StsCoreResponse result = this.facebookService.setupProfile(accessToken, RoleType.GUEST.name());
		return result;
	}

	@POST
	@Path("/setupFacebookProfileWithRole")
	@Produces(MediaType.APPLICATION_JSON)
	public StsCoreResponse setupFacebookProfileWithRole(@QueryParam("accessToken") String accessToken,
			@QueryParam("userType") String userType, @Context HttpServletResponse servletResponse) throws IOException {
		StsCoreResponse result = this.facebookService.setupProfile(accessToken, userType);
		return result;
	}

	public void setFacebookService(FacebookService facebookService) {
		this.facebookService = facebookService;
	}

}
