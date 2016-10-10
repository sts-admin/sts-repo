package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Engineer;
import com.awacp.service.EngineerService;
import com.sts.core.dto.StsResponse;

public class EngineerServiceImpl extends CommonServiceImpl<Engineer> implements EngineerService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Engineer> listEngineers(int pageNumber, int pageSize) {
		return listAll(pageNumber, pageSize, Engineer.class.getSimpleName(), getEntityManager());
	}

	@Override
	public Engineer getEngineer(Long id) {
		return getEntityManager().find(Engineer.class, id);
	}

	@Override
	@Transactional
	public Engineer saveEngineer(Engineer Engineer) {
		getEntityManager().persist(Engineer);
		getEntityManager().flush();
		return Engineer;
	}

	@Override
	@Transactional
	public Engineer updateEngineer(Engineer Engineer) {
		Engineer = getEntityManager().merge(Engineer);
		getEntityManager().flush();
		return Engineer;
	}

}
