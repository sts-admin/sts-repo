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
		StsResponse<Spec> results = this.specService.listSpecs(pageNumber, pageSize);
		if (results != null && results.getResults() != null) {
			for (Spec spec : results.getResults()) {
				System.err.println("Takeoff end point, Spec = " + spec.getDetail());
			}
		}
		return results;
	}

	@GET
	@Path("/getSpecification/{specificationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Spec getSpecification(@PathParam("specId") Long specificationId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.specService.getSpec(specificationId);
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

	public void setSpecService(SpecService specService) {
		this.specService = specService;
	}

}
