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

import com.awacp.entity.Factory;
import com.awacp.service.FactoryService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class FactoryServiceEndpoint extends CrossOriginFilter {

	@Autowired
	FactoryService factoryService;

	@GET
	@Path("/listFactories/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Factory> listFactories(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.factoryService.listFactories(pageNumber, pageSize);
	}

	@GET
	@Path("/getFactory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Factory getFactory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.factoryService.getFactory(id);
	}

	@POST
	@Path("/saveFactory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Factory saveFactory(Factory factory, @Context HttpServletResponse servletResponse) throws Exception {
		return this.factoryService.saveFactory(factory);
	}
	

	@GET
	@Path("/deleteFactory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteFactory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = factoryService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setFactoryService(FactoryService factoryService) {
		this.factoryService = factoryService;
	}

}
