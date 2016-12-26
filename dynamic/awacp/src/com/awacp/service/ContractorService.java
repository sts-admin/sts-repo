package com.awacp.service;

import com.awacp.entity.Contractor;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface ContractorService {
	public StsResponse<Contractor> listContractors(int pageNumber, int pageSize);

	public Contractor getContractor(Long id);

	public Contractor saveContractor(Contractor contractor) throws StsDuplicateException;

	public Contractor updateContractor(Contractor contractor) throws StsDuplicateException;
	
	public String delete(Long id);
}
