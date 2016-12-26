package com.awacp.service;

import java.util.List;

import com.awacp.entity.Architect;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface ArchitectService {

	public Architect updateArchitect(Architect architect) throws StsDuplicateException;

	public Architect saveArchitect(Architect architect) throws StsDuplicateException;

	public Architect getArchitect(Long architectId);

	public StsResponse<Architect> listArchitects(int pageNumber, int pageSize);

	public List<Architect> filter(String keyword); // match name
	
	public String delete(Long id);

}
