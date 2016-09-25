package com.awacp.service;

import java.util.List;

import com.awacp.entity.Takeoff;

public interface TakeoffService {
	public List<Takeoff> listTakeoffs(int pageNumber, int pageSize);

	public Takeoff getTakeoff(Long takeoffId);

	public Takeoff saveTakeoff(Takeoff takeoff);

	public Takeoff updateTakeoff(Takeoff takeoff);
}
