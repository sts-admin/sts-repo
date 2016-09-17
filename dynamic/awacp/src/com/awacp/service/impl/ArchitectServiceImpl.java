package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Architect;
import com.awacp.service.ArchitectService;

public class ArchitectServiceImpl implements ArchitectService {
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
	public List<Architect> listArchitects() {
		return getEntityManager().createNamedQuery("Architect.listAll").getResultList();
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
