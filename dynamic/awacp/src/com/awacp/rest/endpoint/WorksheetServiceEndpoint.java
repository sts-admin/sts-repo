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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.awacp.entity.QuoteMailTracker;
import com.awacp.entity.Worksheet;
import com.awacp.service.WorksheetService;
import com.sts.core.web.filter.CrossOriginFilter;

public class WorksheetServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private WorksheetService worksheetService;

	@GET
	@Path("/listQuoteMailTrackers/{worksheetId}/{takeoffId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<QuoteMailTracker> listQuoteMailTrackers(@PathParam("worksheetId") Long worksheetId,
			@PathParam("takeoffId") Long takeoffId, @Context HttpServletResponse servletResponse) throws IOException {
		return this.worksheetService.listByWorksheetAndTakeoff(worksheetId, takeoffId);
	}

	@GET
	@Path("/sendEmailToBidders/{worksheetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String sendEmailToBidders(@PathParam("worksheetId") Long worksheetId,
			@Context HttpServletResponse servletResponse) throws Exception {
		boolean status = this.worksheetService.sendEmailToBidders(worksheetId);
		return "{\"status\":\"" + status + "\"}";
	}

	@GET
	@Path("/getWorksheet/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Worksheet getWorksheet(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.worksheetService.getWorksheet(id);
	}

	@POST
	@Path("/saveWorksheet")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Worksheet saveWorksheet(Worksheet worksheet, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.worksheetService.saveWorksheet(worksheet);
	}

	@GET
	@Path("/deleteWorksheet/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteWorksheet(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = worksheetService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setWorksheetService(WorksheetService worksheetService) {
		this.worksheetService = worksheetService;
	}

}
