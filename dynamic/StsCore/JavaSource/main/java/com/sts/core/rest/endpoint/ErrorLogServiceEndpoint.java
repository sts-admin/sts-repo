package com.sts.core.rest.endpoint;

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

import com.sts.core.entity.ErrorLog;
import com.sts.core.service.ErrorLogService;
import com.sts.core.web.filter.CrossOriginFilter;

public class ErrorLogServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private ErrorLogService errorLogService;

	@POST
	@Path("/saveErrorLog")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ErrorLog saveErrorLog(com.sts.core.entity.ErrorLog log, @Context HttpServletResponse servletResponse) throws IOException {
		return this.errorLogService.save(log);
	}

	@GET
	@Path("/listErrorLogs")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<ErrorLog> listErrorLogs(@Context HttpServletResponse servletResponse) throws IOException {
		return this.errorLogService.getAll();
	}

	@GET
	@Path("/deleteErrorLogById")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteErrorLogById(@QueryParam("errorLogId") Long errorLogId,
			@Context HttpServletResponse servletResponse) throws IOException {
		this.errorLogService.delete(errorLogId);
	}

	@GET
	@Path("/deleteAllErrorLogs")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Integer deleteAllErrorLogs(@Context HttpServletResponse servletResponse) throws IOException {
		return this.errorLogService.deleteAll();
	}

	@GET
	@Path("/getErrorLogById")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ErrorLog getErrorLogById(@QueryParam("errorLogId") Long errorLogId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.errorLogService.getById(errorLogId);
	}

	public void setErrorLogService(ErrorLogService errorLogService) {
		this.errorLogService = errorLogService;
	}
}
