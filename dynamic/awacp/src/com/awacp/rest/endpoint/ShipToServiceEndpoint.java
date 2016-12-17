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

import com.awacp.entity.ShipTo;
import com.awacp.service.ShipToService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class ShipToServiceEndpoint extends CrossOriginFilter {

	@Autowired
	ShipToService shipToService;

	@GET
	@Path("/listShipTos/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<ShipTo> listShipTos(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.shipToService.listShipTos(pageNumber, pageSize);
	}

	@GET
	@Path("/getShipTo/{shipToId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ShipTo getShipTo(@PathParam("shipToId") Long shipToId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.shipToService.getShipTo(shipToId);
	}

	@POST
	@Path("/saveShipTo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ShipTo saveShipTo(ShipTo shipTo, @Context HttpServletResponse servletResponse) throws Exception {
		return this.shipToService.saveShipTo(shipTo);
	}

	@POST
	@Path("/updateShipTo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ShipTo updateShipTo(ShipTo shipTo, @Context HttpServletResponse servletResponse) throws IOException {
		return this.shipToService.updateShipTo(shipTo);
	}

	public void setShipToService(ShipToService shipToService) {
		this.shipToService = shipToService;
	}

}
