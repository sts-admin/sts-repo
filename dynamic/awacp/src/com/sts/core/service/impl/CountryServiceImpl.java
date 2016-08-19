/**
 * 
 */
package com.sts.core.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sts.core.entity.Country;
import com.sts.core.service.CountryService;

public class CountryServiceImpl implements CountryService {

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
	public List<Country> findAll() {
		return getEntityManager().createNamedQuery("Country.findAll").getResultList();
	}

	@Override
	public Country findById(Long countryId) {
		return getEntityManager().find(Country.class, countryId);
	}
}
