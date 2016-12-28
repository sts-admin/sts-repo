package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.JInventory;
import com.awacp.service.JInventoryService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class JInventoryServiceImpl extends CommonServiceImpl<JInventory>implements JInventoryService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<JInventory> listJInventories(int pageNumber, int pageSize) {
		StsResponse<JInventory> results = listAll(pageNumber, pageSize, JInventory.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public JInventory getJInventory(Long id) {
		return getEntityManager().find(JInventory.class, id);
	}

	@Override
	@Transactional
	public JInventory saveJInventory(JInventory JInventory)  {
		getEntityManager().persist(JInventory);
		getEntityManager().flush();
		return JInventory;
	}

	@Override
	@Transactional
	public JInventory updateJInventory(JInventory JInventory)  {
		getEntityManager().merge(JInventory);
		getEntityManager().flush();
		return JInventory;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		JInventory entity = getJInventory(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}
}
