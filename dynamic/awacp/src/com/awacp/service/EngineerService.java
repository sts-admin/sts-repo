package com.awacp.service;

import java.util.List;

import com.awacp.entity.Engineer;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface EngineerService {

	public StsResponse<Engineer> listEngineers(int pageNumber, int pageSize);

	public Engineer getEngineer(Long engineerId);

	public Engineer saveEngineer(Engineer engineer) throws StsDuplicateException;

	public Engineer updateEngineer(Engineer engineer) throws StsDuplicateException;
	
	public List<Engineer> filter(String keyword); // match name

}
