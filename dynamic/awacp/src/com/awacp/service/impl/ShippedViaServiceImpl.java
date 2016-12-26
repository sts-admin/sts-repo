package com.awacp.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.ShippedVia;
import com.awacp.service.ShippedViaService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class ShippedViaServiceImpl extends CommonServiceImpl<ShippedVia>implements ShippedViaService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<ShippedVia> listShippedVias(int pageNumber, int pageSize) {
		StsResponse<ShippedVia> results = listAll(pageNumber, pageSize, ShippedVia.class.getSimpleName(),
				getEntityManager());

		return results;
	}

	@Override
	public ShippedVia getShippedVia(Long id) {
		return getEntityManager().find(ShippedVia.class, id);
	}

	@Override
	@Transactional
	public ShippedVia saveShippedVia(ShippedVia shippedVia) {
		getEntityManager().persist(shippedVia);
		getEntityManager().flush();
		return shippedVia;
	}

	@Override
	@Transactional
	public ShippedVia updateShippedVia(ShippedVia shippedVia) {
		shippedVia = getEntityManager().merge(shippedVia);
		getEntityManager().flush();
		return shippedVia;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		ShippedVia entity = getShippedVia(id);
		if (entity != null) {
			entity.setArchived(true);
			updateShippedVia(entity);
			return "success";
		}
		return "fail";
	}

}
