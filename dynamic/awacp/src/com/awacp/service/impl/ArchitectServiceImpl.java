package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Architect;
import com.awacp.service.ArchitectService;
import com.sts.core.dto.StsResponse;

public class ArchitectServiceImpl extends CommonServiceImpl<Architect> implements ArchitectService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}
	
	@Override
	public StsResponse<Architect> listArchitects(int pageNumber, int pageSize) {
		return listAll(pageNumber, pageSize, Architect.class.getSimpleName(), getEntityManager());
	}


	@Override
	public Architect getArchitect(Long id) {
		return getEntityManager().find(Architect.class, id);
	}

	@Override
	@Transactional
	public Architect saveArchitect(Architect architect) {
		getEntityManager().persist(architect);
		getEntityManager().flush();
		return architect;
	}

	@Override
	@Transactional
	public Architect updateArchitect(Architect architect) {
		architect = getEntityManager().merge(architect);
		getEntityManager().flush();
		return architect;
	}

}
