package com.sts.core.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.sts.core.entity.Country;
import com.sts.core.service.CountryService;
import com.sts.core.web.filter.CrossOriginFilter;

public class CountryServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private CountryService countryService;

	@GET
	@Path("/listCountries")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Country> listCountries(@Context HttpServletResponse servletResponse) throws IOException {
		return this.countryService.findAll();
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
}
