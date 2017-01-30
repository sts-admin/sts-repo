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

import com.awacp.entity.Takeoff;
import com.awacp.service.SpecService;
import com.awacp.service.TakeoffService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class TakeoffServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private TakeoffService takeoffService;

	@Autowired
	SpecService specService;

	@GET
	@Path("/filterTakeoffs/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Takeoff> filterTakeoffs(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.listTakeoffs(pageNumber, pageSize);
	}

	@GET
	@Path("/listTakeoffsForView/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Takeoff> listTakeoffsForView(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.listTakeoffsForView(pageNumber, pageSize);
	}

	@GET
	@Path("/listNewTakeoffsForQuote/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Takeoff> listNewTakeoffsForQuote(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.listNewTakeoffsForQuote(pageNumber, pageSize);
	}

	@GET
	@Path("/listTakeoffs/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Takeoff> listTakeoffs(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.listTakeoffs(pageNumber, pageSize);
	}

	@GET
	@Path("/getTakeoff/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Takeoff getTakeoff(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.takeoffService.getTakeoff(id);
	}
	
	@GET
	@Path("/getTakeoffWithDetail/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Takeoff getTakeoffWithDetail(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.takeoffService.getTakeoffWithDetail(id);
	}

	@GET
	@Path("/makeQuote/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String makeQuote(@PathParam("id") Long id, @Context HttpServletResponse servletResponse) throws IOException {
		String result = this.takeoffService.makeQuote(id);
		return "{\"result\":\"" + result + "\"}";
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
	@Path("/deleteTakeoff/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteTakeoff(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = takeoffService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setTakeoffService(TakeoffService takeoffService) {
		this.takeoffService = takeoffService;
	}

}
