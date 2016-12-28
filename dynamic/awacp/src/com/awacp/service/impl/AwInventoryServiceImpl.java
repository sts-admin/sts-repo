package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.AwInventory;
import com.awacp.service.AwInventoryService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class AwInventoryServiceImpl extends CommonServiceImpl<AwInventory>implements AwInventoryService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<AwInventory> listAwInventories(int pageNumber, int pageSize) {
		StsResponse<AwInventory> results = listAll(pageNumber, pageSize, AwInventory.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public AwInventory getAwInventory(Long id) {
		return getEntityManager().find(AwInventory.class, id);
	}

	@Override
	@Transactional
	public AwInventory saveAwInventory(AwInventory AwInventory)  {
		getEntityManager().persist(AwInventory);
		getEntityManager().flush();
		return AwInventory;
	}

	@Override
	@Transactional
	public AwInventory updateAwInventory(AwInventory AwInventory)  {
		getEntityManager().merge(AwInventory);
		getEntityManager().flush();
		return AwInventory;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		AwInventory entity = getAwInventory(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}
}
