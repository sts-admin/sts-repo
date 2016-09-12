package com.awacp.service;

import java.util.List;

import com.awacp.entity.Contractor;

public interface ContractorService {
	public List<Contractor> listContractors();

	public Contractor getContractor(Long id);

	public Contractor saveContractor(Contractor contractor);

	public Contractor updateContractor(Contractor contractor);
}
