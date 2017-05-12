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

import com.awacp.entity.PageSetting;
import com.awacp.service.PageSettingService;
import com.sts.core.web.filter.CrossOriginFilter;

public class PageSettingServiceEndpoint extends CrossOriginFilter {

	@Autowired
	PageSettingService pageSettingService;

	@GET
	@Path("/getSiteSetting/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public PageSetting getSiteSetting(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.pageSettingService.getSiteSetting(id);
	}

	@GET
	@Path("/getSiteSettingByView")
	@Produces(MediaType.APPLICATION_JSON)
	public PageSetting getSiteSettingByView(@QueryParam("viewName") String viewName,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.pageSettingService.getSiteSetting(viewName);
	}

	@GET
	@Path("/setPageSizeByView")
	@Produces(MediaType.APPLICATION_JSON)
	public String setPageSizeByView(@QueryParam("viewName") String viewName, @QueryParam("size") Integer size,
			@Context HttpServletResponse servletResponse) throws IOException {
		String result = this.pageSettingService.setPageSizeByView(viewName, size);
		return "{\"result\":\"" + result + "\"}";
	}

	@POST
	@Path("/saveSiteSetting")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public PageSetting saveSiteSetting(PageSetting pageSetting, @Context HttpServletResponse servletResponse)
			throws Exception {
		return this.pageSettingService.saveSiteSetting(pageSetting);
	}

	@POST
	@Path("/updateSiteSetting")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public PageSetting updateSiteSetting(PageSetting pageSetting, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.pageSettingService.updateSiteSetting(pageSetting);
	}

	@GET
	@Path("/deleteSiteSetting/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteSiteSetting(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = pageSettingService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setPageSettingService(PageSettingService pageSettingService) {
		this.pageSettingService = pageSettingService;
	}

}
