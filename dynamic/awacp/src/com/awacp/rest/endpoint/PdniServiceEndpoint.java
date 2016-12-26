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

import com.awacp.entity.Pdni;
import com.awacp.service.PdniService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class PdniServiceEndpoint extends CrossOriginFilter {

	@Autowired
	PdniService pdniService;

	@GET
	@Path("/listPdnis/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Pdni> listPdnis(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.pdniService.listPdnis(pageNumber, pageSize);
	}

	@GET
	@Path("/getPdni/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Pdni getPdni(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.pdniService.getPdni(id);
	}

	@POST
	@Path("/savePdni")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Pdni savePdni(Pdni pdni, @Context HttpServletResponse servletResponse) throws Exception {
		return this.pdniService.savePdni(pdni);
	}

	@POST
	@Path("/updatePdni")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Pdni updatePdni(Pdni pdni, @Context HttpServletResponse servletResponse) throws IOException {
		return this.pdniService.updatePdni(pdni);
	}
	

	@GET
	@Path("/deletePdni/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deletePdni(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = pdniService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setPdniService(PdniService pdniService) {
		this.pdniService = pdniService;
	}

}
