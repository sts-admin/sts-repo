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

import com.awacp.entity.ShippedVia;
import com.awacp.service.ShippedViaService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class ShippedViaServiceEndpoint extends CrossOriginFilter {

	@Autowired
	ShippedViaService shippedViaService;

	@GET
	@Path("/listShippedVias/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<ShippedVia> listShippedVias(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.shippedViaService.listShippedVias(pageNumber, pageSize);
	}

	@GET
	@Path("/getShippedVia/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ShippedVia getShippedVia(@PathParam("id") Long id,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.shippedViaService.getShippedVia(id);
	}
	

	@GET
	@Path("/deleteShippedVia/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteShipTo(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = shippedViaService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@POST
	@Path("/saveShippedVia")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ShippedVia saveShippedVia(ShippedVia shippedVia, @Context HttpServletResponse servletResponse) throws Exception {
		return this.shippedViaService.saveShippedVia(shippedVia);
	}

	@POST
	@Path("/updateShippedVia")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ShippedVia updateShippedVia(ShippedVia shippedVia, @Context HttpServletResponse servletResponse) throws IOException {
		return this.shippedViaService.updateShippedVia(shippedVia);
	}

	public void setShippedViaService(ShippedViaService shippedViaService) {
		this.shippedViaService = shippedViaService;
	}

}
