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

import com.awacp.entity.AwInventory;
import com.awacp.entity.AwfInventory;
import com.awacp.entity.InvMultiplier;
import com.awacp.entity.JInventory;
import com.awacp.entity.SbcInventory;
import com.awacp.entity.SplInventory;
import com.awacp.service.AwInventoryService;
import com.awacp.service.AwfInventoryService;
import com.awacp.service.InvMultiplierService;
import com.awacp.service.JInventoryService;
import com.awacp.service.SbcInventoryService;
import com.awacp.service.SplInventoryService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsCoreException;
import com.sts.core.web.filter.CrossOriginFilter;

public class InventoryServiceEndpoint extends CrossOriginFilter {

	@Autowired
	AwfInventoryService awfInventoryService;

	@Autowired
	AwInventoryService awInventoryService;

	@Autowired
	SplInventoryService splInventoryService;

	@Autowired
	SbcInventoryService sbcInventoryService;

	@Autowired
	JInventoryService jInventoryService;

	@Autowired
	InvMultiplierService invMultiplierService;

	@GET
	@Path("/listAwfInventories/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<AwfInventory> listAwfInventories(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.awfInventoryService.listAwfInventories(pageNumber, pageSize);
	}

	@GET
	@Path("/listAwInventories/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<AwInventory> listAwInventories(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.awInventoryService.listAwInventories(pageNumber, pageSize);
	}

	@GET
	@Path("/listSplInventories/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<SplInventory> listSplInventories(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.splInventoryService.listSplInventories(pageNumber, pageSize);
	}

	@GET
	@Path("/listSbcInventories/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<SbcInventory> listSbcInventories(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.sbcInventoryService.listSbcInventories(pageNumber, pageSize);
	}

	@GET
	@Path("/listJInventories/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<JInventory> listJInventories(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.jInventoryService.listJInventories(pageNumber, pageSize);
	}

	@GET
	@Path("/getAwInventory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public AwInventory getAwInventory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.awInventoryService.getAwInventory(id);
	}

	@GET
	@Path("/getAwfInventory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public AwfInventory getAwfInventory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.awfInventoryService.getAwfInventory(id);
	}

	@GET
	@Path("/getSplInventory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public SplInventory getSplInventory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.splInventoryService.getSplInventory(id);
	}

	@GET
	@Path("/getSbcInventory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public SbcInventory getSbcInventory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.sbcInventoryService.getSbcInventory(id);
	}

	@GET
	@Path("/getJInventory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public JInventory getJInventory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.jInventoryService.getJInventory(id);
	}

	@POST
	@Path("/saveAwInventory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AwInventory saveAwInventory(AwInventory awInventory, @Context HttpServletResponse servletResponse)
			throws Exception {
		return this.awInventoryService.saveAwInventory(awInventory);
	}

	@POST
	@Path("/saveAwfInventory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AwfInventory saveAwfInventory(AwfInventory awfInventory, @Context HttpServletResponse servletResponse)
			throws Exception {
		return this.awfInventoryService.saveAwfInventory(awfInventory);
	}

	@POST
	@Path("/saveSplInventory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SplInventory saveSplInventory(SplInventory splInventory, @Context HttpServletResponse servletResponse)
			throws Exception {
		return this.splInventoryService.saveSplInventory(splInventory);
	}

	@POST
	@Path("/saveSbcInventory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SbcInventory saveSbcInventory(SbcInventory sbcInventory, @Context HttpServletResponse servletResponse)
			throws Exception {
		return this.sbcInventoryService.saveSbcInventory(sbcInventory);
	}

	@POST
	@Path("/saveJInventory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JInventory saveJInventory(JInventory jInventory, @Context HttpServletResponse servletResponse)
			throws Exception {
		return this.jInventoryService.saveJInventory(jInventory);
	}

	@POST
	@Path("/updateJInventory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JInventory updateJInventory(JInventory jInventory, @Context HttpServletResponse servletResponse) {
		return this.jInventoryService.updateJInventory(jInventory);
	}

	@POST
	@Path("/updateSplInventory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SplInventory updateSplInventory(SplInventory splInventory, @Context HttpServletResponse servletResponse) {
		return this.splInventoryService.updateSplInventory(splInventory);
	}

	@POST
	@Path("/updateSbcInventory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SbcInventory updateSbcInventory(SbcInventory sbcInventory, @Context HttpServletResponse servletResponse) {
		return this.sbcInventoryService.updateSbcInventory(sbcInventory);
	}

	@POST
	@Path("/updateAwfInventory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AwfInventory updateAwfInventory(AwfInventory awfInventory, @Context HttpServletResponse servletResponse) {
		return this.awfInventoryService.updateAwfInventory(awfInventory);
	}

	@POST
	@Path("/updateAwInventory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AwInventory updateAwInventory(AwInventory awInventory, @Context HttpServletResponse servletResponse) {
		return this.awInventoryService.updateAwInventory(awInventory);
	}

	@GET
	@Path("/deleteAwInventory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAwInventory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = awInventoryService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/deleteAwfInventory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAwfInventory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = awfInventoryService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/deleteSbcInventory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteSbcInventory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = sbcInventoryService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/deleteSplInventory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteSplInventory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = splInventoryService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/deleteJInventory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteJInventory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = jInventoryService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/listInvMultipliers/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<InvMultiplier> listInvMultipliers(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.invMultiplierService.listInvMultipliers(pageNumber, pageSize);
	}

	@GET
	@Path("/getInvMultiplier/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public InvMultiplier getInvMultiplier(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.invMultiplierService.getInvMultiplier(id);
	}

	@POST
	@Path("/saveInvMultiplier")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public InvMultiplier saveInvMultiplier(InvMultiplier invMultiplier, @Context HttpServletResponse servletResponse)
			throws Exception {
		InvMultiplier object = null;
		try {
			object = this.invMultiplierService.saveInvMultiplier(invMultiplier);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_MULTIPLIER.toLowerCase())) {
				code = 1200;
			}
			servletResponse.sendError(code, message);

		}
		return object;

	}

	@POST
	@Path("/updateInvMultiplier")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public InvMultiplier updateInvMultipliern(InvMultiplier invMultiplier, @Context HttpServletResponse servletResponse)
			throws IOException {
		InvMultiplier object = null;
		try {
			object = this.invMultiplierService.updateInvMultiplier(invMultiplier);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_MULTIPLIER.toLowerCase())) {
				code = 1200;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	@GET
	@Path("/deleteInvMultiplier/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteInvMultiplier(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = invMultiplierService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setInvMultiplierService(InvMultiplierService invMultiplierService) {
		this.invMultiplierService = invMultiplierService;
	}

	public void setAwInventoryService(AwInventoryService awInventoryService) {
		this.awInventoryService = awInventoryService;
	}

	public void setAwfInventoryService(AwfInventoryService awfInventoryService) {
		this.awfInventoryService = awfInventoryService;
	}

	public void setSplInventoryService(SplInventoryService splInventoryService) {
		this.splInventoryService = splInventoryService;
	}

	public void setSbcInventoryService(SbcInventoryService sbcInventoryService) {
		this.sbcInventoryService = sbcInventoryService;
	}

	public void setjInventoryService(JInventoryService jInventoryService) {
		this.jInventoryService = jInventoryService;
	}

}
