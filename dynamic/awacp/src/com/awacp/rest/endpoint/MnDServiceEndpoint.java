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

import com.awacp.entity.MnD;
import com.awacp.service.MnDService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class MnDServiceEndpoint extends CrossOriginFilter {

	@Autowired
	MnDService mnDService;

	@GET
	@Path("/listMnDs/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<MnD> listMnDs(@PathParam("pageNumber") int pageNumber, @PathParam("pageSize") int pageSize,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.mnDService.listMnDs(pageNumber, pageSize);
	}

	@GET
	@Path("/getMnD/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public MnD getMnD(@PathParam("id") Long id, @Context HttpServletResponse servletResponse) throws IOException {
		return this.mnDService.getMnD(id);
	}

	@POST
	@Path("/saveMnD")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public MnD saveMnD(MnD mnD, @Context HttpServletResponse servletResponse) throws Exception {
		return this.mnDService.saveMnD(mnD);
	}

	@POST
	@Path("/updateMnD")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public MnD updateMnD(MnD mnD, @Context HttpServletResponse servletResponse) throws IOException {
		return this.mnDService.updateMnD(mnD);
	}
	

	@GET
	@Path("/deleteMnD/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteMnD(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = mnDService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setMnDService(MnDService mnDService) {
		this.mnDService = mnDService;
	}

}
