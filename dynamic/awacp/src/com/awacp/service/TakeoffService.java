package com.awacp.service;

import java.util.List;

import com.awacp.entity.Takeoff;
import com.sts.core.dto.StsResponse;

public interface TakeoffService {
	public List<StsResponse> listTakeoffs(int pageNumber, int pageSize);

	public Takeoff getTakeoff(Long takeoffId);

	public Takeoff saveTakeoff(Takeoff takeoff);

	public Takeoff updateTakeoff(Takeoff takeoff);
}
