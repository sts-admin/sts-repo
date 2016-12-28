package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.AwfInventory;
import com.awacp.service.AwfInventoryService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class AwfInventoryServiceImpl extends CommonServiceImpl<AwfInventory>implements AwfInventoryService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<AwfInventory> listAwfInventories(int pageNumber, int pageSize) {
		StsResponse<AwfInventory> results = listAll(pageNumber, pageSize, AwfInventory.class.getSimpleName(),
				getEntityManager());

		return results;
	}

	@Override
	public AwfInventory getAwfInventory(Long id) {
		return getEntityManager().find(AwfInventory.class, id);
	}

	@Override
	@Transactional
	public AwfInventory saveAwfInventory(AwfInventory AwfInventory) {
		getEntityManager().persist(AwfInventory);
		getEntityManager().flush();
		return AwfInventory;
	}

	@Override
	@Transactional
	public AwfInventory updateAwfInventory(AwfInventory AwfInventory) {
		getEntityManager().merge(AwfInventory);
		getEntityManager().flush();
		return AwfInventory;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		AwfInventory entity = getAwfInventory(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}
}
