package com.awacp.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.awacp.entity.Contractor;
import com.awacp.service.ContractorService;
import com.sts.core.web.filter.CrossOriginFilter;

public class ContractorServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private ContractorService contractorService;

	@GET
	@Path("/listContractors")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contractor> listContractors(@Context HttpServletResponse servletResponse) throws IOException {
		return this.contractorService.listContractors();
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

}
