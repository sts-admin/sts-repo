package com.awacp.service;

import java.util.List;

import com.awacp.entity.InvMultiplier;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsCoreException;

public interface InvMultiplierService {

	public InvMultiplier updateInvMultiplier(InvMultiplier InvMultiplier) throws StsCoreException;

	public InvMultiplier saveInvMultiplier(InvMultiplier InvMultiplier) throws StsCoreException;

	public InvMultiplier getInvMultiplier(Long id);

	public StsResponse<InvMultiplier> listInvMultipliers(int pageNumber, int pageSize);

	public List<InvMultiplier> filter(String keyword); // match name
	
	public String delete(Long id);

}
