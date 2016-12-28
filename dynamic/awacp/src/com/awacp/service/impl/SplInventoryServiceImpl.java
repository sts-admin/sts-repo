package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.SplInventory;
import com.awacp.service.SplInventoryService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class SplInventoryServiceImpl extends CommonServiceImpl<SplInventory>implements SplInventoryService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<SplInventory> listSplInventories(int pageNumber, int pageSize) {
		StsResponse<SplInventory> results = listAll(pageNumber, pageSize, SplInventory.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public SplInventory getSplInventory(Long id) {
		return getEntityManager().find(SplInventory.class, id);
	}

	@Override
	@Transactional
	public SplInventory saveSplInventory(SplInventory SplInventory){
		getEntityManager().persist(SplInventory);
		getEntityManager().flush();
		return SplInventory;
	}

	@Override
	@Transactional
	public SplInventory updateSplInventory(SplInventory SplInventory) {
		getEntityManager().merge(SplInventory);
		getEntityManager().flush();
		return SplInventory;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		SplInventory entity = getSplInventory(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}
}
