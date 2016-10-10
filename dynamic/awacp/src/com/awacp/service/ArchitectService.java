package com.awacp.service;

import com.awacp.entity.Architect;
import com.sts.core.dto.StsResponse;

public interface ArchitectService {

	public Architect updateArchitect(Architect architect);

	public Architect saveArchitect(Architect architect);

	public Architect getArchitect(Long architectId);
	public StsResponse<Architect> listArchitects(int pageNumber, int pageSize);

}
