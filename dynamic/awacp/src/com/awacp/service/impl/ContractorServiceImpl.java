package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Architect;
import com.awacp.entity.Contractor;
import com.awacp.entity.GeneralContractor;
import com.awacp.service.ContractorService;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.service.impl.CommonServiceImpl;

public class ContractorServiceImpl extends CommonServiceImpl<Contractor>implements ContractorService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<com.awacp.entity.Contractor> listContractors(int pageNumber, int pageSize) {
		return listAll(pageNumber, pageSize, GeneralContractor.class.getSimpleName(), getEntityManager());
	}

	@Override
	public Contractor getContractor(Long id) {
		return getEntityManager().find(Contractor.class, id);
	}

	@Override
	@Transactional
	public Contractor saveContractor(Contractor contractor) throws StsDuplicateException {
		if (isExistsByEmail(contractor.getEmail(), "Contractor", getEntityManager())) {
			throw new StsDuplicateException("duplicate_email");
		}
		getEntityManager().merge(contractor);
		getEntityManager().flush();
		return contractor;
	}

	@Override
	@Transactional
	public Contractor updateContractor(Contractor contractor) throws StsDuplicateException {
		Contractor object = getByEmail(contractor.getEmail(), "Contractor", getEntityManager());
		if (object != null && object.getId() != contractor.getId()) {
			throw new StsDuplicateException("duplicate_email");
		}
		getEntityManager().merge(contractor);
		getEntityManager().flush();
		return contractor;
	}

}
