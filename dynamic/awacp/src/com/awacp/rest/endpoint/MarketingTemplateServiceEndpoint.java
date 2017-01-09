package com.awacp.rest.endpoint;

import java.io.IOException;
import java.util.List;

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

import com.awacp.entity.MarketingTemplate;
import com.awacp.service.MarketingTemplateService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsCoreException;
import com.sts.core.web.filter.CrossOriginFilter;

public class MarketingTemplateServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private MarketingTemplateService marketingTemplateService;

	@GET
	@Path("/listMarketingTemplates/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<MarketingTemplate> listMarketingTemplates(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.marketingTemplateService.listMarketingTemplates(pageNumber, pageSize);

	}

	@GET
	@Path("/getMarketingTemplate/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public MarketingTemplate getMarketingTemplate(@PathParam("id") Long marketingTemplateId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.marketingTemplateService.getMarketingTemplate(marketingTemplateId);
	}

	@POST
	@Path("/saveMarketingTemplate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public MarketingTemplate saveMarketingTemplate(MarketingTemplate marketingTemplate, @Context HttpServletResponse servletResponse)
			throws IOException {
		MarketingTemplate object = null;
		try {
			object = this.marketingTemplateService.saveMarketingTemplate(marketingTemplate);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_TEMPLATE.toLowerCase())) {
				code = 5000;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}

	@POST
	@Path("/updateMarketingTemplate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public MarketingTemplate updateMarketingTemplate(MarketingTemplate marketingTemplate, @Context HttpServletResponse servletResponse)
			throws IOException {
		MarketingTemplate object = null;
		try {
			object = this.marketingTemplateService.updateMarketingTemplate(marketingTemplate);
		} catch (StsCoreException e) {
			Integer code = 500;
			final String message = e.getMessage().toLowerCase();
			if (e.getMessage().equals(StsCoreConstant.DUPLICATE_TEMPLATE.toLowerCase())) {
				code = 5000;
			}
			servletResponse.sendError(code, message);

		}
		return object;
	}
	
	@GET
	@Path("/deleteMarketingTemplate/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteMarketingTemplate(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = marketingTemplateService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setMarketingTemplateService(MarketingTemplateService marketingTemplateService) {
		this.marketingTemplateService = marketingTemplateService;
	}

}
