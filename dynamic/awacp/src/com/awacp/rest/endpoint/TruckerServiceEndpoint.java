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

import com.awacp.entity.Trucker;
import com.awacp.service.TruckerService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class TruckerServiceEndpoint extends CrossOriginFilter {

	@Autowired
	TruckerService truckerService;

	@GET
	@Path("/listTruckers/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Trucker> listTruckers(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.truckerService.listTruckers(pageNumber, pageSize);
	}

	@GET
	@Path("/getTrucker/{truckerId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Trucker getTrucker(@PathParam("truckerId") Long truckerId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.truckerService.getTrucker(truckerId);
	}

	@POST
	@Path("/saveTrucker")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Trucker saveTrucker(Trucker trucker, @Context HttpServletResponse servletResponse) throws Exception {
		return this.truckerService.saveTrucker(trucker);
	}

	@POST
	@Path("/updateTrucker")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Trucker updateTrucker(Trucker trucker, @Context HttpServletResponse servletResponse) throws IOException {
		return this.truckerService.updateTrucker(trucker);
	}

	public void setTruckerService(TruckerService truckerService) {
		this.truckerService = truckerService;
	}

}
