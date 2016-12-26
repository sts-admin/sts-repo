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

import com.awacp.entity.ItemShipped;
import com.awacp.service.ItemShippedService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class ItemShippedServiceEndpoint extends CrossOriginFilter {

	@Autowired
	ItemShippedService itemShippedService;

	@GET
	@Path("/listItemShippeds/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<ItemShipped> listItemShippeds(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.itemShippedService.listItemShippeds(pageNumber, pageSize);
	}

	@GET
	@Path("/getItemShipped/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ItemShipped getItemShipped(@PathParam("id") Long id,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.itemShippedService.getItemShipped(id);
	}

	@GET
	@Path("/deleteItemShipped/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteItemShipped(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = itemShippedService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@POST
	@Path("/saveItemShipped")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ItemShipped saveItemShipped(ItemShipped itemShipped, @Context HttpServletResponse servletResponse)
			throws Exception {
		return this.itemShippedService.saveItemShipped(itemShipped);
	}

	@POST
	@Path("/updateItemShipped")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ItemShipped updateItemShipped(ItemShipped itemShipped, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.itemShippedService.updateItemShipped(itemShipped);
	}

	public void setItemShippedService(ItemShippedService itemShippedService) {
		this.itemShippedService = itemShippedService;
	}

}
