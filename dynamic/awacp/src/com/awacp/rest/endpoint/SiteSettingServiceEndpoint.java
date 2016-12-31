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

import com.awacp.entity.SiteSetting;
import com.awacp.service.SiteSettingService;
import com.sts.core.web.filter.CrossOriginFilter;

public class SiteSettingServiceEndpoint extends CrossOriginFilter {

	@Autowired
	SiteSettingService siteSettingService;

	@GET
	@Path("/getSiteSetting/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public SiteSetting getSiteSetting(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.siteSettingService.getSiteSetting(id);
	}

	@GET
	@Path("/getSiteSettingByView")
	@Produces(MediaType.APPLICATION_JSON)
	public SiteSetting getSiteSettingByView(@QueryParam("viewName") String viewName,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.siteSettingService.getSiteSetting(viewName);
	}

	@GET
	@Path("/setPageSizeByView")
	@Produces(MediaType.APPLICATION_JSON)
	public String setPageSizeByView(@QueryParam("viewName") String viewName, @QueryParam("size") Integer size,
			@Context HttpServletResponse servletResponse) throws IOException {
		String result = this.siteSettingService.setPageSizeByView(viewName, size);
		return "{\"result\":\"" + result + "\"}";
	}

	@POST
	@Path("/saveSiteSetting")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SiteSetting saveSiteSetting(SiteSetting siteSetting, @Context HttpServletResponse servletResponse)
			throws Exception {
		return this.siteSettingService.saveSiteSetting(siteSetting);
	}

	@POST
	@Path("/updateSiteSetting")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SiteSetting updateSiteSetting(SiteSetting siteSetting, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.siteSettingService.updateSiteSetting(siteSetting);
	}

	@GET
	@Path("/deleteSiteSetting/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteSiteSetting(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = siteSettingService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setSiteSettingService(SiteSettingService siteSettingService) {
		this.siteSettingService = siteSettingService;
	}

}
