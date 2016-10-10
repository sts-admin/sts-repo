package com.awacp.service;

import com.awacp.entity.Engineer;
import com.sts.core.dto.StsResponse;

public interface EngineerService {

	public StsResponse<Engineer> listEngineers(int pageNumber, int pageSize);

	public Engineer getEngineer(Long engineerId);

	public Engineer saveEngineer(Engineer engineer);

	public Engineer updateEngineer(Engineer engineer);

}
