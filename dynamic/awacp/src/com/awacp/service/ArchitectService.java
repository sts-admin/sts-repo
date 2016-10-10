package com.awacp.service;

import java.util.List;

import com.awacp.entity.Architect;
import com.sts.core.dto.StsResponse;

public interface ArchitectService {

	public Architect updateArchitect(Architect architect);

	public Architect saveArchitect(Architect architect);

	public Architect getArchitect(Long architectId);

	public StsResponse<com.awacp.entity.Architect> listArchitects(int pageNumber , int pageSize);

}
