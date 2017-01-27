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

import com.awacp.entity.Worksheet;
import com.awacp.service.WorksheetService;
import com.sts.core.web.filter.CrossOriginFilter;

public class WorksheetServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private WorksheetService worksheetService;

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

	@POST
	@Path("/updateWorksheet")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Worksheet updateWorksheet(Worksheet worksheet, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.worksheetService.updateWorksheet(worksheet);
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
