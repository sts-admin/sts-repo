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

import com.awacp.entity.Spec;
import com.awacp.service.SpecService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class SpecServiceEndpoint extends CrossOriginFilter {

	@Autowired
	SpecService specService;

	@GET
	@Path("/listSpecifications/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Spec> listSpecifications(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.specService.listSpecs(pageNumber, pageSize);
	}

	@GET
	@Path("/getSpecification/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Spec getSpecification(@PathParam("id") Long id,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.specService.getSpec(id);
	}

	@POST
	@Path("/saveSpecification")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Spec saveSpecification(Spec spec, @Context HttpServletResponse servletResponse) throws Exception {
		return this.specService.saveSpec(spec);
	}

	@POST
	@Path("/updateSpecification")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Spec updateSpecification(Spec spec, @Context HttpServletResponse servletResponse) throws IOException {
		return this.specService.updateSpec(spec);
	}
	

	@GET
	@Path("/deleteSpec/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteSpec(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = specService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setSpecService(SpecService specService) {
		this.specService = specService;
	}

}
