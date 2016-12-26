package com.awacp.service;

import com.awacp.entity.Pdni;
import com.sts.core.dto.StsResponse;

public interface PdniService {

	public Pdni updatePdni(Pdni Pdni);

	public Pdni savePdni(Pdni Pdni);

	public Pdni getPdni(Long PdniId);

	public StsResponse<Pdni> listPdnis(int pageNumber, int pageSize);
	
	public String delete(Long id);

}
