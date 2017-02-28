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

import com.awacp.entity.TaxEntry;
import com.awacp.service.TaxService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class TaxServiceEndpoint extends CrossOriginFilter {

	@Autowired
	TaxService taxService;

	@GET
	@Path("/listTaxEntries/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<TaxEntry> listTaxEntrys(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.taxService.listTaxEntries(pageNumber, pageSize);
	}

	@GET
	@Path("/getTaxEntry/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public TaxEntry getTaxEntry(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.taxService.getTaxEntry(id);
	}

	@POST
	@Path("/saveTaxEntry")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TaxEntry saveTaxEntry(TaxEntry taxEntry, @Context HttpServletResponse servletResponse) throws Exception {
		return this.taxService.saveTaxEntry(taxEntry);
	}

	@POST
	@Path("/updateTaxEntry")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TaxEntry updateTaxEntry(TaxEntry taxEntry, @Context HttpServletResponse servletResponse) throws IOException {
		return this.taxService.updateTaxEntry(taxEntry);
	}
	

	@GET
	@Path("/deleteTaxEntry/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteTaxEntry(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = taxService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setTaxService(TaxService taxService) {
		this.taxService = taxService;
	}

}
