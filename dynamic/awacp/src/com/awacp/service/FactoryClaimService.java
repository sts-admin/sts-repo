package com.awacp.service;

import com.awacp.entity.FactoryClaim;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface FactoryClaimService {

	public FactoryClaim updateFactoryClaim(FactoryClaim claim) throws StsDuplicateException;;

	public FactoryClaim saveFactoryClaim(FactoryClaim claim) throws StsDuplicateException;;

	public FactoryClaim getFactoryClaim(Long id);

	public StsResponse<FactoryClaim> listFactoryClaims(int pageNumber, int pageSize);

	public String delete(Long id);

}
