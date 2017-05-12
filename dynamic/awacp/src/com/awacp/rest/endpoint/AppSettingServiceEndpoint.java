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

import com.awacp.entity.AppSetting;
import com.awacp.service.AppSettingService;
import com.sts.core.web.filter.CrossOriginFilter;

public class AppSettingServiceEndpoint extends CrossOriginFilter {

	@Autowired
	AppSettingService appSettingService;

	@GET
	@Path("/getAppSetting/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public AppSetting getAppSetting(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.appSettingService.getAppSetting(id);
	}

	@POST
	@Path("/saveAppSetting")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AppSetting saveAppSetting(AppSetting appSetting, @Context HttpServletResponse servletResponse)
			throws Exception {
		return this.appSettingService.saveAppSetting(appSetting);
	}

	@POST
	@Path("/updateAppSetting")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public AppSetting updateAppSetting(AppSetting appSetting, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.appSettingService.updateAppSetting(appSetting);
	}

	@GET
	@Path("/deleteAppSetting/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAppSetting(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = appSettingService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setAppSettingService(AppSettingService appSettingService) {
		this.appSettingService = appSettingService;
	}

}
