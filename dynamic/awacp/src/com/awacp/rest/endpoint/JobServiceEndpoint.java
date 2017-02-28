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

import com.awacp.entity.JobOrder;
import com.awacp.service.JobService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class JobServiceEndpoint extends CrossOriginFilter {

	@Autowired
	JobService jobService;

	@GET
	@Path("/listJobOrders/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<JobOrder> listJobOrders(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.jobService.listJobOrders(pageNumber, pageSize);
	}

	@GET
	@Path("/getJobOrder/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public JobOrder getJobOrder(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.jobService.getJobOrder(id);
	}

	@POST
	@Path("/saveJobOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JobOrder saveJobOrder(JobOrder JobOrder, @Context HttpServletResponse servletResponse) throws Exception {
		return this.jobService.saveJobOrder(JobOrder);
	}

	@POST
	@Path("/updateJobOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JobOrder updateJobOrder(JobOrder jobOrder, @Context HttpServletResponse servletResponse) throws Exception {
		return this.jobService.updateJobOrder(jobOrder);
	}
	

	@GET
	@Path("/deleteJobOrder/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteJobOrder(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = this.jobService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}

}
