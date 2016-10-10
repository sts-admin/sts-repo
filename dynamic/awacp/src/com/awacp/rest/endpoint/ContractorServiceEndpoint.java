package com.awacp.rest.endpoint;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.awacp.entity.Bidder;
import com.awacp.entity.Contractor;
import com.awacp.service.BidderService;
import com.awacp.service.ContractorService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class ContractorServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private ContractorService contractorService;

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
		return this.bidderService.saveBidder(bidder);
	}

	@POST
	@Path("/updateBidder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Bidder updateBidder(Bidder bidder, @Context HttpServletResponse servletResponse) throws IOException {
		return this.bidderService.updateBidder(bidder);
	}

	@GET
	@Path("/getContractor")
	@Produces(MediaType.APPLICATION_JSON)
	public Contractor getContractor(@QueryParam("contractorId") Long contractorId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.contractorService.getContractor(contractorId);
	}

	@POST
	@Path("/saveContractor")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Contractor saveContractor(Contractor contractor, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.contractorService.saveContractor(contractor);
	}

	@POST
	@Path("/updateContractor")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Contractor updateContractor(Contractor contractor, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.contractorService.updateContractor(contractor);
	}

	public void setContractorService(ContractorService contractorService) {
		this.contractorService = contractorService;
	}

	public void setBidderService(BidderService bidderService) {
		this.bidderService = bidderService;
	}
}
