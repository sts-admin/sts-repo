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

import com.awacp.entity.Invoice;
import com.awacp.entity.JobOrder;
import com.awacp.service.InvoiceService;
import com.awacp.service.JobService;
import com.sts.core.constant.StsErrorCode;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsResourceNotFoundException;
import com.sts.core.web.filter.CrossOriginFilter;

public class JobServiceEndpoint extends CrossOriginFilter {

	@Autowired
	JobService jobService;

	@Autowired
	InvoiceService invoiceService;

	@GET
	@Path("/getInvoiceByJobOrderId/{jobOrderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Invoice getInvoiceByJobOrderId(@PathParam("jobOrderId") Long jobOrderId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.invoiceService.getInvoiceByJobOrder(jobOrderId);
	}

	@GET
	@Path("/getInvoice/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Invoice getInvoice(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.invoiceService.getInvoice(id);
	}

	@POST
	@Path("/saveInvoice")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Invoice saveInvoice(Invoice invoice, @Context HttpServletResponse servletResponse) throws Exception {
		return this.invoiceService.saveInvoice(invoice);
	}

	@GET
	@Path("/deleteInvoice/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteInvoice(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = this.invoiceService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/listJobOrders/{invoiceStatus}/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<JobOrder> listJobOrders(@PathParam("invoiceStatus") String invoiceStatus,
			@PathParam("pageNumber") int pageNumber, @PathParam("pageSize") int pageSize,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.jobService.listJobOrdersByInvoiceStatus(pageNumber, pageSize, invoiceStatus);
	}

	@GET
	@Path("/searchQuoteForJobOrder/{quoteId}")
	@Produces(MediaType.APPLICATION_JSON)
	public JobOrder searchQuoteForJobOrder(@PathParam("quoteId") String quoteId,
			@Context HttpServletResponse servletResponse) throws Exception {
		JobOrder object = null;
		Integer code = StsErrorCode.DEFAULT_CODE;
		try {
			object = this.jobService.searchQuoteForJobOrder(quoteId);
		} catch (StsResourceNotFoundException e) {
			final String message = e.getMessage().toLowerCase();
			code = StsErrorCode.RESOURCE_NOT_FOUND;
			servletResponse.sendError(code, message);
		}
		return object;
	}

	@GET
	@Path("/getJobOrderByOrderNumber/{orderNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public JobOrder getJobOrderByOrderId(@PathParam("orderNumber") String orderNumber,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.jobService.getJobOrderByOrderId(orderNumber);
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
	public JobOrder saveJobOrder(JobOrder jobOrder, @Context HttpServletResponse servletResponse) throws Exception {
		return this.jobService.saveJobOrder(jobOrder);
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

	public void setInvoiceService(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}

}
