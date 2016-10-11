package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Contractor;
import com.awacp.entity.GeneralContractor;
import com.awacp.service.ContractorService;
import com.sts.core.dto.StsResponse;

public class ContractorServiceImpl implements ContractorService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StsResponse<com.awacp.entity.Contractor> listContractors(int pageNumber , int pageSize) {
		StsResponse<com.awacp.entity.Contractor> response = new StsResponse<com.awacp.entity.Contractor>();
		if (pageNumber <= 1) {
			Object object = getEntityManager().createNamedQuery("Contractor.countAll").getSingleResult();
			if (object != null) {
				response.setTotalCount(((Long) object).intValue());
			}
		}
		Query query = getEntityManager().createNamedQuery("Contractor.listAll");
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<Contractor> contractors = query.getResultList();
		return contractors == null || contractors.isEmpty() ? response : response.setResults(contractors);

	}


	@Override
	public Contractor getContractor(Long id) {
		return getEntityManager().find(Contractor.class, id);
	}

	@Override
	@Transactional
	public Contractor saveContractor(Contractor contractor) {
		getEntityManager().persist(contractor);
		getEntityManager().flush();
		return contractor;
	}

	@Override
	@Transactional
	public Contractor updateContractor(Contractor contractor) {
		contractor = getEntityManager().merge(contractor);
		getEntityManager().flush();
		return contractor;
	}

	@Override
	public GeneralContractor getGenearalContractor(Long id) {
		return getEntityManager().find(GeneralContractor.class, id);
	}

}
