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

import com.awacp.entity.Specification;
import com.awacp.entity.Takeoff;
import com.awacp.service.SpecificationService;
import com.awacp.service.TakeoffService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class TakeoffServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private TakeoffService takeoffService;

	@Autowired
	SpecificationService specificationService;

	@GET
	@Path("/listTakeoffs/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Takeoff> listTakeoffs(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.listTakeoffs(pageNumber, pageSize);
	}

	@GET
	@Path("/getTakeoff/{takeoffId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Takeoff getTakeoff(@PathParam("takeoffId") Long takeoffId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.takeoffService.getTakeoff(takeoffId);
	}

	@POST
	@Path("/saveTakeoff")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Takeoff saveTakeoff(Takeoff takeoff, @Context HttpServletResponse servletResponse) throws Exception {
		return this.takeoffService.saveTakeoff(takeoff);
	}

	@POST
	@Path("/updateTakeoff")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Takeoff updateTakeoff(Takeoff takeoff, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.updateTakeoff(takeoff);
	}

	@GET
	@Path("/listSpecifications/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Specification> listSpecifications(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.specificationService.listSpecifications(pageNumber, pageSize);
	}

	@GET
	@Path("/getSpecification/{specificationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Specification getSpecification(@PathParam("specificationId") Long specificationId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.specificationService.getSpecification(specificationId);
	}

	@POST
	@Path("/saveSpecification")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Specification saveSpecification(Specification specification, @Context HttpServletResponse servletResponse)
			throws Exception {
		return this.specificationService.saveSpecification(specification);
	}

	@POST
	@Path("/updateSpecification")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Specification updateSpecification(Specification specification, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.specificationService.updateSpecification(specification);
	}

	public void setTakeoffService(TakeoffService takeoffService) {
		this.takeoffService = takeoffService;
	}

	public void setSpecificationService(SpecificationService specificationService) {
		this.specificationService = specificationService;
	}

}
