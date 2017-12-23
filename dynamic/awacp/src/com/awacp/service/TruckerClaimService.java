package com.awacp.service;

import com.awacp.entity.TruckerClaim;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface TruckerClaimService {

	public TruckerClaim updateTruckerClaim(TruckerClaim claim) throws StsDuplicateException;;

	public TruckerClaim saveTruckerClaim(TruckerClaim claim) throws StsDuplicateException;;

	public TruckerClaim getTruckerClaim(Long id);

	public StsResponse<TruckerClaim> listTruckerClaims(int pageNumber, int pageSize);

	public String delete(Long id);

}
