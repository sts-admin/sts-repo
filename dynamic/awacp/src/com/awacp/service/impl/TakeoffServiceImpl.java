package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Takeoff;
import com.awacp.service.TakeoffService;

public class TakeoffServiceImpl implements TakeoffService {

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
	public List<Takeoff> listTakeoffs() {
		return getEntityManager().createNamedQuery("Takeoff.listAll").getResultList();
	}

	@Override
	public Takeoff getTakeoff(Long takeoffId) {
		return getEntityManager().find(Takeoff.class, takeoffId);
	}

	@Override
	@Transactional
	public Takeoff saveTakeoff(Takeoff takeoff) {
		getEntityManager().persist(takeoff);
		getEntityManager().flush();
		return takeoff;
	}

	@Override
	@Transactional
	public Takeoff updateTakeoff(Takeoff takeoff) {
		takeoff = getEntityManager().merge(takeoff);
		getEntityManager().flush();
		return takeoff;
	}

}
