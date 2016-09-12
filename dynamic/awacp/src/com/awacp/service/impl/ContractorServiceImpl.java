package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Contractor;
import com.awacp.service.ContractorService;

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
	public List<Contractor> listContractors() {
		return getEntityManager().createNamedQuery("Contractor.listAll").getResultList();
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

}
