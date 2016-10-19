package com.awacp.rest.endpoint;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.awacp.entity.Engineer;
import com.awacp.service.EngineerService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsCoreException;
import com.sts.core.web.filter.CrossOriginFilter;

public class EngineerServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private EngineerService engineerService;

	@GET
	@Path("/listEngineers/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Engineer> listEngineer(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.engineerService.listEngineers(pageNumber, pageSize);
	}

	@GET
	@Path("/getEngineer/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Engineer getEngineer(@PathParam("id") Long engineerId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.engineerService.getEngineer(engineerId);
	}

	@POST
	@Path("/saveEngineer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Engineer saveEngineer(Engineer engineer, @Context HttpServletResponse servletResponse) throws IOException {
		Engineer object = null;
		try {
			object = this.engineerService.saveEngineer(engineer);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = 1002;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	@POST
	@Path("/updateEngineer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Engineer updateEngineer(Engineer engineer, @Context HttpServletResponse servletResponse) throws IOException {
		Engineer object = null;
		try {
			object = this.engineerService.updateEngineer(engineer);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = 1002;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	public void setEngineerService(EngineerService engineerService) {
		this.engineerService = engineerService;
	}

}
