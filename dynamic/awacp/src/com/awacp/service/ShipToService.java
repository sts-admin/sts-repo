package com.awacp.service;

import java.util.List;

import com.awacp.entity.ShipTo;
import com.sts.core.dto.StsResponse;

public interface ShipToService {

	public ShipTo updateShipTo(ShipTo ShipTo);

	public ShipTo saveShipTo(ShipTo ShipTo);

	public ShipTo getShipTo(Long architectId);

	public StsResponse<ShipTo> listShipTos(int pageNumber, int pageSize);

	public List<ShipTo> filter(String keyword); // match name
	
	public String delete(Long id);

}
