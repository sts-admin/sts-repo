package com.awacp.service;

import com.awacp.entity.ShippedVia;
import com.sts.core.dto.StsResponse;

public interface ShippedViaService {

	public ShippedVia updateShippedVia(ShippedVia ShippedVia);

	public ShippedVia saveShippedVia(ShippedVia ShippedVia);

	public ShippedVia getShippedVia(Long architectId);

	public StsResponse<ShippedVia> listShippedVias(int pageNumber, int pageSize);
	
	public String delete(Long id);

}
