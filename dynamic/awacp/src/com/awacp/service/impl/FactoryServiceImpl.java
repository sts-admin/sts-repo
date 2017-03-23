/**
 * 
 */
package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Factory;
import com.awacp.service.FactoryService;
import com.sts.core.dto.StsResponse;

public class FactoryServiceImpl implements FactoryService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@Transactional
	public Factory saveFactory(Factory factory) {

		if (factory.getId() != null) {
			getEntityManager().merge(factory);
		} else {
			getEntityManager().persist(factory);
		}
		getEntityManager().flush();
		return factory;
	}

	@Override
	public Factory getFactory(Long factoryId) {
		return getEntityManager().find(Factory.class, factoryId);
	}

	@Override
	public StsResponse<Factory> listFactories(int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		Factory entity = getFactory(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}
}
