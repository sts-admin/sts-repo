package com.awacp.rest.endpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.awacp.service.TakeoffService;
import com.sts.core.web.filter.CrossOriginFilter;

public class TakeoffServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private TakeoffService takeoffService;
	
	
	public void setTakeoffService(TakeoffService takeoffService) {
		this.takeoffService = takeoffService;
	}

}
