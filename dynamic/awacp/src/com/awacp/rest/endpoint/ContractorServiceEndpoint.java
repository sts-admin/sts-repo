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

import com.awacp.entity.Bidder;
import com.awacp.entity.Contractor;
import com.awacp.entity.GeneralContractor;
import com.awacp.service.BidderService;
import com.awacp.service.ContractorService;
import com.awacp.service.GeneralContractorService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.constant.StsErrorCode;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsCoreException;
import com.sts.core.web.filter.CrossOriginFilter;

public class ContractorServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private ContractorService contractorService;

	@Autowired
	private GeneralContractorService generalContractorService;

	@Autowired
	private BidderService bidderService;

	@GET
	@Path("/listContractors/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Contractor> listContractors(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.contractorService.listContractors(pageNumber, pageSize);
	}

	@GET
	@Path("/listGcs/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<GeneralContractor> listGcs(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.generalContractorService.listContractors(pageNumber, pageSize);
	}

	@GET
	@Path("/listBidders/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Bidder> listBidders(@PathParam("pageNumber") int pageNumber, @PathParam("pageSize") int pageSize,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.bidderService.listBidders(pageNumber, pageSize);
	}

	@GET
	@Path("/getBidder/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Bidder getBidder(@PathParam("id") Long id, @Context HttpServletResponse servletResponse) throws IOException {
		return this.bidderService.getBidder(id);
	}

	@POST
	@Path("/saveBidder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Bidder saveBidder(Bidder bidder, @Context HttpServletResponse servletResponse) throws IOException {
		Bidder object = null;
		try {
			object = this.bidderService.saveBidder(bidder);
		} catch (StsCoreException e) {
			Integer code = StsErrorCode.DEFAULT_CODE;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = StsErrorCode.DUPLICATE_EMAIL;
			} else if (e.getMessage().equals(StsCoreConstant.DUPLICATE_NAME.toLowerCase())) {
				code = StsErrorCode.DUPLICATE_NAME;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	@POST
	@Path("/updateBidder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Bidder updateBidder(Bidder bidder, @Context HttpServletResponse servletResponse) throws IOException {

		Bidder object = null;
		try {
			object = this.bidderService.updateBidder(bidder);
		} catch (StsCoreException e) {
			Integer code = StsErrorCode.DEFAULT_CODE;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = StsErrorCode.DUPLICATE_EMAIL;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	@GET
	@Path("/deleteContractor/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteContractor(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = contractorService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(StsErrorCode.RESOURCE_DELETE, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/deleteGc/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteGc(@PathParam("id") Long id, @Context HttpServletResponse servletResponse) throws IOException {
		String result = generalContractorService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(StsErrorCode.RESOURCE_DELETE, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/deleteBidder/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteBidder(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = bidderService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(StsErrorCode.RESOURCE_DELETE, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/getContractor/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Contractor getContractor(@PathParam("id") Long contractorId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.contractorService.getContractor(contractorId);
	}

	@GET
	@Path("/getGc/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public GeneralContractor getGc(@PathParam("id") Long contractorId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.generalContractorService.getContractor(contractorId);
	}

	@POST
	@Path("/saveContractor")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Contractor saveContractor(Contractor contractor, @Context HttpServletResponse servletResponse)
			throws IOException {
		Contractor object = null;
		try {
			object = this.contractorService.saveContractor(contractor);
		} catch (StsCoreException e) {
			Integer code = StsErrorCode.DEFAULT_CODE;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = StsErrorCode.DUPLICATE_EMAIL;
			}else if (e.getMessage().equals(StsCoreConstant.DUPLICATE_NAME.toLowerCase())) {
				code = StsErrorCode.DUPLICATE_NAME;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	@POST
	@Path("/updateContractor")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Contractor updateContractor(Contractor contractor, @Context HttpServletResponse servletResponse)
			throws IOException {
		Contractor object = null;
		try {
			object = this.contractorService.updateContractor(contractor);
		} catch (StsCoreException e) {
			Integer code = StsErrorCode.DEFAULT_CODE;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = StsErrorCode.DUPLICATE_EMAIL;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	@POST
	@Path("/saveGc")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public GeneralContractor saveGc(GeneralContractor generalContractor, @Context HttpServletResponse servletResponse)
			throws IOException {
		GeneralContractor object = null;
		try {
			object = this.generalContractorService.updateContractor(generalContractor);
		} catch (StsCoreException e) {
			Integer code = StsErrorCode.DEFAULT_CODE;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = StsErrorCode.DUPLICATE_EMAIL;
			}else if (e.getMessage().equals(StsCoreConstant.DUPLICATE_NAME.toLowerCase())) {
				code = StsErrorCode.DUPLICATE_NAME;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	@POST
	@Path("/updateGc")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public GeneralContractor updateGc(GeneralContractor generalContractor, @Context HttpServletResponse servletResponse)
			throws IOException {
		GeneralContractor object = null;
		try {
			object = this.generalContractorService.updateContractor(generalContractor);
		} catch (StsCoreException e) {
			Integer code = StsErrorCode.DEFAULT_CODE;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_EMAIL.toLowerCase())) {
				code = StsErrorCode.DUPLICATE_EMAIL;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	public void setContractorService(ContractorService contractorService) {
		this.contractorService = contractorService;
	}

	public void setGeneralContractorService(GeneralContractorService generalContractorService) {
		this.generalContractorService = generalContractorService;
	}

	public void setBidderService(BidderService bidderService) {
		this.bidderService = bidderService;
	}
}
