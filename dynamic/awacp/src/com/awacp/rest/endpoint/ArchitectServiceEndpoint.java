package com.awacp.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import com.awacp.entity.Architect;
import com.awacp.entity.Bidder;
import com.awacp.service.ArchitectService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class ArchitectServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private ArchitectService architectService;

	@GET
	@Path("/listArchitects/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Architect> listArchitect(@PathParam("pageNumber") int pageNumber, @PathParam("pageSize") int pageSize,@Context HttpServletResponse servletResponse) throws IOException {
		return this.architectService.listArchitects(pageNumber, pageSize);
	}

	@GET
	@Path("/getArchitect")
	@Produces(MediaType.APPLICATION_JSON)
	public Architect getArchitect(@QueryParam("ArchitectId") Long ArchitectId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.architectService.getArchitect(ArchitectId);
	}

	@POST
	@Path("/saveArchitect")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Architect saveArchitect(Architect architect, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.architectService.saveArchitect(architect);
	}

	@POST
	@Path("/updateArchitect")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Architect updateArchitect(Architect architect, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.architectService.updateArchitect(architect);
	}

	public void setArchitectService(ArchitectService architectService) {
		this.architectService = architectService;
	}

}
