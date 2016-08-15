package com.sts.core.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.sts.core.entity.State;
import com.sts.core.service.StateService;
import com.sts.core.web.filter.CrossOriginFilter;

public class StateServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private StateService stateService;

	@GET
	@Path("/listStates")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<State> listStates(@Context HttpServletResponse servletResponse) throws IOException {
		return this.stateService.findAll();
	}

	@GET
	@Path("/listStatesByCountry/{countryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<State> listStatesByCountry(@PathParam("countryId") Long countryId, @Context HttpServletResponse servletResponse) throws IOException {
		return this.stateService.findAllByCountry(countryId);
	}

	public void setStateService(StateService stateService) {
		this.stateService = stateService;
	}
}
