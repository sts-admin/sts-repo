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

import com.awacp.entity.Architect;
import com.awacp.service.ArchitectService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsCoreException;
import com.sts.core.web.filter.CrossOriginFilter;

public class ArchitectServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private ArchitectService architectService;

	@GET
	@Path("/listArchitects/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Architect> listArchitect(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.architectService.listArchitects(pageNumber, pageSize);

	}

	@GET
	@Path("/getArchitect/{architectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Architect getArchitect(@PathParam("architectId") Long architectId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.architectService.getArchitect(architectId);
	}

	@POST
	@Path("/saveArchitect")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Architect saveArchitect(Architect architect, @Context HttpServletResponse servletResponse)
			throws IOException {
		Architect object = null;
		try {
			object = this.architectService.saveArchitect(architect);
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
	@Path("/updateArchitect")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Architect updateArchitect(Architect architect, @Context HttpServletResponse servletResponse)
			throws IOException {
		Architect object = null;
		try {
			object = this.architectService.updateArchitect(architect);
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

	public void setArchitectService(ArchitectService architectService) {
		this.architectService = architectService;
	}

}
