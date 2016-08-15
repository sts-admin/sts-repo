/**
 * 
 */
package com.sts.core.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sts.core.entity.State;
import com.sts.core.service.StateService;

public class StateServiceImpl implements StateService {

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
	public List<State> findAll() {
		return getEntityManager().createNamedQuery("State.findAll").getResultList();
	}

	@Override
	public State findById(Long stateId) {
		return getEntityManager().find(State.class, stateId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<State> findAllByCountry(Long countryId) {
		return getEntityManager().createNamedQuery("State.findByCountryId").setParameter("countryId", countryId).getResultList();
	}
}
