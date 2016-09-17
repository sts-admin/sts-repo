package com.awacp.rest.endpoint;

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
import com.awacp.entity.Engineer;
import com.awacp.service.EngineerService;
import com.sts.core.web.filter.CrossOriginFilter;

public class EngineerServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private EngineerService EngineerService;

	@GET
	@Path("/listEngineers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Engineer> listEngineer(@Context HttpServletResponse servletResponse) throws IOException {
		return this.EngineerService.listEngineers();
	}

	@GET
	@Path("/getEngineer")
	@Produces(MediaType.APPLICATION_JSON)
	public Engineer getEngineer(@QueryParam("EngineerId") Long EngineerId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.EngineerService.getEngineer(EngineerId);
	}

	@POST
	@Path("/saveEngineer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Engineer saveEngineer(Engineer Engineer, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.EngineerService.saveEngineer(Engineer);
	}

	@POST
	@Path("/updateEngineer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Engineer updateEngineer(Engineer Engineer, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.EngineerService.updateEngineer(Engineer);
	}

	public void setEngineerService(EngineerService EngineerService) {
		this.EngineerService = EngineerService;
	}

}
