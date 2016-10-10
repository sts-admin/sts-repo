package com.awacp.service;

import java.util.List;

import com.awacp.entity.Contractor;
import com.awacp.entity.GeneralContractor;
import com.sts.core.dto.StsResponse;

public interface ContractorService {
	public StsResponse<com.awacp.entity.Contractor> listContractors(int pageNumber , int pageSize);

	public Contractor getContractor(Long id);
	
	public GeneralContractor getGenearalContractor(Long id);

	public Contractor saveContractor(Contractor contractor);

	public Contractor updateContractor(Contractor contractor);
}
