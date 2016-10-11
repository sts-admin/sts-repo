package com.awacp.service;

import com.awacp.entity.Takeoff;
import com.sts.core.dto.StsResponse;

public interface TakeoffService {
	public StsResponse<Takeoff> listTakeoffs(int pageNumber, int pageSize);

	public Takeoff getTakeoff(Long takeoffId);

	public Takeoff saveTakeoff(Takeoff takeoff) throws Exception;

	public Takeoff updateTakeoff(Takeoff takeoff);

	public String[] getNewTakeoffEmails(Long takeoffId);
}
