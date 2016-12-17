package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Trucker;
import com.awacp.service.TruckerService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class TruckerServiceImpl extends CommonServiceImpl<Trucker>implements TruckerService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Trucker> listTruckers(int pageNumber, int pageSize) {
		StsResponse<Trucker> results = listAll(pageNumber, pageSize, Trucker.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public Trucker getTrucker(Long id) {
		return getEntityManager().find(Trucker.class, id);
	}

	@Override
	@Transactional
	public Trucker saveTrucker(Trucker Trucker) {
		getEntityManager().persist(Trucker);
		getEntityManager().flush();
		return Trucker;
	}

	@Override
	@Transactional
	public Trucker updateTrucker(Trucker Trucker) {
		Trucker = getEntityManager().merge(Trucker);
		getEntityManager().flush();
		return Trucker;
	}
}
