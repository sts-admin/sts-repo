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

import com.awacp.entity.ShipmentStatus;
import com.awacp.entity.Tracking;
import com.awacp.entity.Trucker;
import com.awacp.entity.TruckerClaim;
import com.awacp.service.TrackingService;
import com.awacp.service.TruckerClaimService;
import com.awacp.service.TruckerService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.constant.StsErrorCode;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsCoreException;
import com.sts.core.web.filter.CrossOriginFilter;

public class TruckerServiceEndpoint extends CrossOriginFilter {

	@Autowired
	TruckerService truckerService;

	@Autowired
	TruckerClaimService truckerClaimService;

	@Autowired
	TrackingService trackingService;

	/* Tracking: begin */

	@GET
	@Path("/getShipingStatus/{orderBookId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ShipmentStatus getShipingStatus(@PathParam("orderBookId") Long orderBookId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.trackingService.shipmentStatus(orderBookId);
	}

	@GET
	@Path("/getTrackingByOrderNumber/{orderBookId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tracking getTrackingByOrderNumber(@PathParam("orderBookId") Long orderBookId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.trackingService.getTrackingByOrderNumber(orderBookId);
	}

	@GET
	@Path("/getTracking/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Tracking getTracking(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.trackingService.getTracking(id);
	}

	@POST
	@Path("/saveTracking")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Tracking saveTracking(Tracking tracking, @Context HttpServletResponse servletResponse) throws Exception {
		Tracking object = null;
		try {
			System.err.println(this.getClass().getName() + ":saveTracking: begin");
			object = this.trackingService.saveTracking(tracking);
			System.err.println(this.getClass().getName() + ":saveTracking: end");
		} catch (Exception e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().contains(StsCoreConstant.UNKNOWN_SERVICE_PROVIDER.toLowerCase())) {
				code = StsErrorCode.UNKNOWN_SERVICE_PROVIDER;
			}
			System.err.println("ERROR: " + e.getMessage());
			servletResponse.sendError(code, message);
		}
		return object;
	}

	@GET
	@Path("/deleteTracking/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteTracking(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = trackingService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	/* Tracking: end */

	/* Trucker claim: begin */

	@GET
	@Path("/listTruckerClaims/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<TruckerClaim> listTruckerClaims(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.truckerClaimService.listTruckerClaims(pageNumber, pageSize);
	}

	@GET
	@Path("/getTruckerClaim/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public TruckerClaim getTruckerClaim(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.truckerClaimService.getTruckerClaim(id);
	}

	@POST
	@Path("/saveTruckerClaim")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TruckerClaim saveTruckerClaim(TruckerClaim truckerClaim, @Context HttpServletResponse servletResponse)
			throws Exception {
		TruckerClaim object = null;
		try {
			object = this.truckerClaimService.updateTruckerClaim(truckerClaim);
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
	@Path("/updateTruckerClaim")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public TruckerClaim updateTruckerClaim(TruckerClaim truckerClaim, @Context HttpServletResponse servletResponse)
			throws IOException {
		TruckerClaim object = null;
		try {
			object = this.truckerClaimService.updateTruckerClaim(truckerClaim);
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
	@Path("/deleteTruckerClaim/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteTruckerClaim(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = truckerClaimService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	/* Trucker claim: end */

	@GET
	@Path("/listTruckers/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Trucker> listTruckers(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.truckerService.listTruckers(pageNumber, pageSize);
	}

	@GET
	@Path("/getTrucker/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Trucker getTrucker(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.truckerService.getTrucker(id);
	}

	@POST
	@Path("/saveTrucker")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Trucker saveTrucker(Trucker trucker, @Context HttpServletResponse servletResponse) throws Exception {
		Trucker object = null;
		try {
			object = this.truckerService.updateTrucker(trucker);
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
	@Path("/updateTrucker")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Trucker updateTrucker(Trucker trucker, @Context HttpServletResponse servletResponse) throws IOException {
		Trucker object = null;
		try {
			object = this.truckerService.updateTrucker(trucker);
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
	@Path("/deleteTrucker/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteTrucker(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = truckerService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setTruckerService(TruckerService truckerService) {
		this.truckerService = truckerService;
	}

	public void setTruckerClaimService(TruckerClaimService truckerClaimService) {
		this.truckerClaimService = truckerClaimService;
	}

	public void setTrackingService(TrackingService trackingService) {
		this.trackingService = trackingService;
	}

}
