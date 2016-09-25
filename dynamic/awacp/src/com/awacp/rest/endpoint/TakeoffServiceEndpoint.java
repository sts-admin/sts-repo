package com.awacp.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.awacp.entity.Takeoff;
import com.awacp.service.TakeoffService;
import com.sts.core.web.filter.CrossOriginFilter;

public class TakeoffServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private TakeoffService takeoffService;

	@GET
	@Path("/listTakeoffs")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Takeoff> listTakeoffs(@QueryParam("pageNumber") int pageNumber, @QueryParam("pageSize") int pageSize,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.listTakeoffs(pageNumber, pageSize);
	}

	@GET
	@Path("/getTakeoff")
	@Produces(MediaType.APPLICATION_JSON)
	public Takeoff getTakeoff(@QueryParam("takeoffId") Long takeoffId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.takeoffService.getTakeoff(takeoffId);
	}

	@POST
	@Path("/saveTakeoff")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Takeoff saveTakeoff(Takeoff takeoff, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.saveTakeoff(takeoff);
	}

	@POST
	@Path("/updateTakeoff")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Takeoff updateTakeoff(Takeoff takeoff, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.updateTakeoff(takeoff);
	}

	public void setTakeoffService(TakeoffService takeoffService) {
		this.takeoffService = takeoffService;
	}

}
