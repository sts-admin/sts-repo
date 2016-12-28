package com.awacp.service;

import com.awacp.entity.Trucker;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface TruckerService {

	public Trucker updateTrucker(Trucker Trucker) throws StsDuplicateException;;

	public Trucker saveTrucker(Trucker Trucker) throws StsDuplicateException;;

	public Trucker getTrucker(Long truckerId);

	public StsResponse<Trucker> listTruckers(int pageNumber, int pageSize);
	
	public String delete(Long id);

}
