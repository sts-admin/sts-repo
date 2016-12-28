package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.SbcInventory;
import com.awacp.service.SbcInventoryService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class SbcInventoryServiceImpl extends CommonServiceImpl<SbcInventory>implements SbcInventoryService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<SbcInventory> listSbcInventories(int pageNumber, int pageSize) {
		StsResponse<SbcInventory> results = listAll(pageNumber, pageSize, SbcInventory.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public SbcInventory getSbcInventory(Long id) {
		return getEntityManager().find(SbcInventory.class, id);
	}

	@Override
	@Transactional
	public SbcInventory saveSbcInventory(SbcInventory SbcInventory)  {
		getEntityManager().persist(SbcInventory);
		getEntityManager().flush();
		return SbcInventory;
	}

	@Override
	@Transactional
	public SbcInventory updateSbcInventory(SbcInventory SbcInventory)  {
		getEntityManager().merge(SbcInventory);
		getEntityManager().flush();
		return SbcInventory;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		SbcInventory entity = getSbcInventory(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}
}
