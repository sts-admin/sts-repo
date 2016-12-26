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

import com.awacp.entity.QuoteNote;
import com.awacp.service.QuoteNoteService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class QuoteNoteServiceEndpoint extends CrossOriginFilter {

	@Autowired
	QuoteNoteService quoteNoteService;

	@GET
	@Path("/listQuoteNotes/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<QuoteNote> listQuoteNotes(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.quoteNoteService.listQuoteNotes(pageNumber, pageSize);
	}

	@GET
	@Path("/getQuoteNote/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public QuoteNote getQuoteNote(@PathParam("id") Long id,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.quoteNoteService.getQuoteNote(id);
	}
	
	@GET
	@Path("/deleteQuoteNote/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteQuoteNote(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		quoteNoteService.delete(id);
		return "success";
	}

	@POST
	@Path("/saveQuoteNote")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public QuoteNote saveQuoteNote(QuoteNote quoteNote, @Context HttpServletResponse servletResponse) throws Exception {
		return this.quoteNoteService.saveQuoteNote(quoteNote);
	}

	@POST
	@Path("/updateQuoteNote")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public QuoteNote updateQuoteNote(QuoteNote quoteNote, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.quoteNoteService.updateQuoteNote(quoteNote);
	}

	public void setQuoteNoteService(QuoteNoteService quoteNoteService) {
		this.quoteNoteService = quoteNoteService;
	}

}
