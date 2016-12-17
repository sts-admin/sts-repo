package com.awacp.service;

import com.awacp.entity.Trucker;
import com.sts.core.dto.StsResponse;

public interface TruckerService {

	public Trucker updateTrucker(Trucker Trucker);

	public Trucker saveTrucker(Trucker Trucker);

	public Trucker getTrucker(Long truckerId);

	public StsResponse<Trucker> listTruckers(int pageNumber, int pageSize);

}
