package com.awacp.rest.endpoint;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

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

import com.awacp.entity.QuoteFollowup;
import com.awacp.entity.Takeoff;
import com.awacp.entity.Worksheet;
import com.awacp.service.SpecService;
import com.awacp.service.TakeoffService;
import com.awacp.service.WorksheetService;
import com.awacp.util.QuotePdfGenerator;
import com.sts.core.config.AppPropConfig;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class TakeoffServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private TakeoffService takeoffService;

	@Autowired
	private WorksheetService worksheetService;

	@Autowired
	SpecService specService;

	@GET
	@Path("/generatePdfUrl/{worksheetId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String generatePdfUrl(@PathParam("worksheetId") Long worksheetId) throws Exception {
		String fileName = "quote-" + Calendar.getInstance().getTimeInMillis() + ".pdf";
		String pdfFilePath = AppPropConfig.resourceWritePath + fileName;
		String logoPath = AppPropConfig.resourceWritePath + "awacp_big_logo.png";
		Worksheet worksheet = worksheetService.getWorksheet(worksheetId);
		if (worksheet.getTakeoff() == null) {
			worksheet.setTakeoff(takeoffService.getTakeoff(worksheet.getTakeoffId()));
		}
		new QuotePdfGenerator(pdfFilePath, logoPath, worksheet).generate();
		/*
		 * FileInputStream fileInputStream = new FileInputStream(new
		 * File(pdfFilePath)); javax.ws.rs.core.Response.ResponseBuilder
		 * responseBuilder = javax.ws.rs.core.Response .ok((Object)
		 * fileInputStream); responseBuilder.type("application/pdf");
		 * responseBuilder.header("Content-Disposition", "inline; filename=" +
		 * fileName);
		 */
		String fileUrl = AppPropConfig.resourceReadPath + fileName;
		return "{\"fileUrl\":\"" + fileUrl + "\"}";
	}

	@GET
	@Path("/getAllQuoteFollowups/{takeoffId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<QuoteFollowup> getAllQuoteFollowups(@PathParam("takeoffId") Long takeoffId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.getAllQuoteFollowups(takeoffId);
	}

	@POST
	@Path("/saveQuoteFollowup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String saveQuoteFollowup(QuoteFollowup quoteFollowup, @Context HttpServletResponse servletResponse)
			throws Exception {
		Long id = this.takeoffService.saveQuoteFollowup(quoteFollowup);
		return "{\"id\":\"" + id + "\"}";
	}

	@GET
	@Path("/filterTakeoffs/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Takeoff> filterTakeoffs(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.listTakeoffs(pageNumber, pageSize);
	}

	@GET
	@Path("/listTakeoffsForView/{pageNumber}/{pageSize}/{quoteView}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Takeoff> listTakeoffsForView(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @PathParam("quoteView") boolean quoteView,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.listTakeoffsForView(pageNumber, pageSize, quoteView);
	}

	@GET
	@Path("/listNewTakeoffsForQuote/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Takeoff> listNewTakeoffsForQuote(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.listNewTakeoffsForQuote(pageNumber, pageSize);
	}

	@GET
	@Path("/listTakeoffs/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<Takeoff> listTakeoffs(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.listTakeoffs(pageNumber, pageSize);
	}

	@GET
	@Path("/getTakeoff/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Takeoff getTakeoff(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.takeoffService.getTakeoff(id);
	}

	@GET
	@Path("/getTakeoffWithDetail/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Takeoff getTakeoffWithDetail(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.takeoffService.getTakeoffWithDetail(id);
	}

	@GET
	@Path("/makeQuote/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String makeQuote(@PathParam("id") Long id, @Context HttpServletResponse servletResponse) throws IOException {
		String result = this.takeoffService.makeQuote(id);
		return "{\"result\":\"" + result + "\"}";
	}

	@POST
	@Path("/saveTakeoff")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Takeoff saveTakeoff(Takeoff takeoff, @Context HttpServletResponse servletResponse) throws Exception {
		return this.takeoffService.saveTakeoff(takeoff);
	}

	@POST
	@Path("/updateTakeoff")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Takeoff updateTakeoff(Takeoff takeoff, @Context HttpServletResponse servletResponse) throws IOException {
		return this.takeoffService.updateTakeoff(takeoff);
	}

	@GET
	@Path("/deleteTakeoff/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteTakeoff(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = takeoffService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setTakeoffService(TakeoffService takeoffService) {
		this.takeoffService = takeoffService;
	}

}
