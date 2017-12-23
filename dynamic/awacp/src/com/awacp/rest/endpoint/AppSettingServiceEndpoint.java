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

import com.awacp.entity.MenuItem;
import com.awacp.entity.SiteColor;
import com.awacp.entity.SiteEmailAccount;
import com.awacp.entity.SiteInfo;
import com.awacp.entity.SystemLog;
import com.awacp.service.AppSettingService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class AppSettingServiceEndpoint extends CrossOriginFilter {

	@Autowired
	AppSettingService appSettingService;
	
	@GET
	@Path("/listSystemLogs/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<SystemLog> listSystemLogs(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.appSettingService.listSystemLogs(pageNumber, pageSize);

	}

	@POST
	@Path("/saveSiteInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SiteInfo saveSiteInfo(SiteInfo siteInfo, @Context HttpServletResponse servletResponse) throws Exception {
		return this.appSettingService.saveSiteInfo(siteInfo);
	}

	@GET
	@Path("/getSiteInfo/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public SiteInfo getSiteInfo(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.appSettingService.getSiteInfo(id);
	}

	@GET
	@Path("/getSiteInfo")
	@Produces(MediaType.APPLICATION_JSON)
	public SiteInfo getSiteInfo(@Context HttpServletResponse servletResponse) throws IOException {
		return this.appSettingService.getSiteInfo();
	}

	@GET
	@Path("/deleteSiteInfo/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteSiteInfo(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String status = this.appSettingService.deleteSiteInfo(id);
		if ("fail".equalsIgnoreCase(status)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + status + "\"}";
	}

	@POST
	@Path("/saveSiteColor")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SiteColor saveSiteColor(SiteColor siteColor, @Context HttpServletResponse servletResponse) throws Exception {
		return this.appSettingService.saveSiteColor(siteColor);
	}

	@GET
	@Path("/getSiteColor/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public SiteColor getSiteColor(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.appSettingService.getSiteColor(id);
	}

	@GET
	@Path("/getSiteColor")
	@Produces(MediaType.APPLICATION_JSON)
	public SiteColor getSiteColor(@Context HttpServletResponse servletResponse) throws IOException {
		return this.appSettingService.getSiteColor();
	}

	@GET
	@Path("/deleteSiteColor/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteSiteColor(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String status = this.appSettingService.deleteSiteColor(id);
		if ("fail".equalsIgnoreCase(status)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + status + "\"}";
	}

	@POST
	@Path("/saveSiteEmailAccount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SiteEmailAccount saveSiteEmailAccount(SiteEmailAccount siteEmailAccount,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.appSettingService.saveSiteEmailAccount(siteEmailAccount);
	}

	@GET
	@Path("/getSiteEmailAccount/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public SiteEmailAccount getSiteEmailAccount(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.appSettingService.getSiteEmailAccount(id);
	}

	@GET
	@Path("/getSiteEmailAccount")
	@Produces(MediaType.APPLICATION_JSON)
	public SiteEmailAccount getSiteEmailAccount(@Context HttpServletResponse servletResponse) throws IOException {
		return this.appSettingService.getSiteEmailAccount();
	}

	@GET
	@Path("/deleteSiteEmailAccount/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteSiteEmailAccount(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = appSettingService.deleteSiteEmailAccount(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	@POST
	@Path("/saveSiteMenuItem")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public MenuItem saveSiteMenuItem(MenuItem menuItem, @Context HttpServletResponse servletResponse) throws Exception {
		return this.appSettingService.saveSiteMenuItem(menuItem);
	}

	@GET
	@Path("/getSiteMenuItem/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public MenuItem getSiteMenuItem(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.appSettingService.getSiteMenuItem(id);
	}

	@GET
	@Path("/getSiteMenuItem")
	@Produces(MediaType.APPLICATION_JSON)
	public MenuItem getSiteMenuItem(@Context HttpServletResponse servletResponse) throws IOException {
		return this.appSettingService.getSiteMenuItem();
	}

	public void setAppSettingService(AppSettingService appSettingService) {
		this.appSettingService = appSettingService;
	}

}
