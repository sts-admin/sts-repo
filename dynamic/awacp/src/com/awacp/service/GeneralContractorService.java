package com.awacp.service;

import com.awacp.entity.GeneralContractor;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface GeneralContractorService {
	public StsResponse<GeneralContractor> listContractors(int pageNumber, int pageSize);

	public GeneralContractor getContractor(Long id);

	public GeneralContractor saveContractor(GeneralContractor contractor) throws StsDuplicateException;

	public GeneralContractor updateContractor(GeneralContractor contractor) throws StsDuplicateException;
	
	public String delete(Long id);
}
