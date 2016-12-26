package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.ItemShipped;
import com.awacp.service.ItemShippedService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class ItemShippedServiceImpl extends CommonServiceImpl<ItemShipped>implements ItemShippedService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<ItemShipped> listItemShippeds(int pageNumber, int pageSize) {
		StsResponse<ItemShipped> results = listAll(pageNumber, pageSize, ItemShipped.class.getSimpleName(),
				getEntityManager());

		return results;
	}

	@Override
	public ItemShipped getItemShipped(Long id) {
		return getEntityManager().find(ItemShipped.class, id);
	}

	@Override
	@Transactional
	public ItemShipped saveItemShipped(ItemShipped itemShipped) {
		getEntityManager().persist(itemShipped);
		getEntityManager().flush();
		return itemShipped;
	}

	@Override
	@Transactional
	public ItemShipped updateItemShipped(ItemShipped itemShipped) {
		itemShipped = getEntityManager().merge(itemShipped);
		getEntityManager().flush();
		return itemShipped;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		ItemShipped entity = getItemShipped(id);
		if (entity != null) {
			entity.setArchived(true);
			updateItemShipped(entity);
			return "success";
		}
		return "fail";
	}

}
