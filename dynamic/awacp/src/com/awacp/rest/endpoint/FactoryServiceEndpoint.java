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

import com.awacp.entity.Factory;
import com.awacp.entity.FactoryClaim;
import com.awacp.service.FactoryClaimService;
import com.awacp.service.FactoryService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.constant.StsErrorCode;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsCoreException;
import com.sts.core.web.filter.CrossOriginFilter;

public class FactoryServiceEndpoint extends CrossOriginFilter {

	@Autowired
	FactoryService factoryService;

	@Autowired
	FactoryClaimService factoryClaimService;

	/* Factory claim: begin */
	

	@GET
	@Path("/listFactoryClaims/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<FactoryClaim> listFactoryClaims(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.factoryClaimService.listFactoryClaims(pageNumber, pageSize);
	}

	@GET
	@Path("/getFactoryClaim/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public FactoryClaim getFactoryClaim(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.factoryClaimService.getFactoryClaim(id);
	}

	@POST
	@Path("/saveFactoryClaim")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public FactoryClaim saveFactoryClaim(FactoryClaim claim, @Context HttpServletResponse servletResponse)
			throws Exception {
		FactoryClaim object = null;
		try {
			object = this.factoryClaimService.updateFactoryClaim(claim);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = 1002;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	@POST
	@Path("/updateFactoryClaim")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public FactoryClaim updateFactoryClaim(FactoryClaim claim, @Context HttpServletResponse servletResponse)
			throws IOException {
		FactoryClaim object = null;
		try {
			object = this.factoryClaimService.updateFactoryClaim(claim);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = 1002;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	@GET
	@Path("/deleteFactoryCliam/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteFactoryClaim(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = factoryClaimService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	/* Factory claim: end */

	@GET
	@Path("/listFactories/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Factory> listFactories(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.factoryService.listFactories(pageNumber, pageSize);
	}

	@GET
	@Path("/getFactory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Factory getFactory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.factoryService.getFactory(id);
	}

	@POST
	@Path("/saveFactory")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Factory saveFactory(Factory factory, @Context HttpServletResponse servletResponse) throws Exception {
		Factory object = null;
		try {
			object = this.factoryService.saveFactory(factory);
		} catch (StsCoreException e) {
			Integer code = StsErrorCode.DEFAULT_CODE;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_NAME)) {
				code = StsErrorCode.DUPLICATE_NAME;
			}
			servletResponse.sendError(code, message);

		}
		return object;

	}

	@GET
	@Path("/deleteFactory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteFactory(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = factoryService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setFactoryService(FactoryService factoryService) {
		this.factoryService = factoryService;
	}

	public void setFactoryClaimService(FactoryClaimService factoryClaimService) {
		this.factoryClaimService = factoryClaimService;
	}

}
