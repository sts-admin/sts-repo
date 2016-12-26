package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.ShipTo;
import com.awacp.service.ShipToService;
import com.sts.core.dto.StsResponse;
import com.sts.core.service.impl.CommonServiceImpl;

public class ShipToServiceImpl extends CommonServiceImpl<ShipTo>implements ShipToService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<ShipTo> listShipTos(int pageNumber, int pageSize) {
		StsResponse<ShipTo> results = listAll(pageNumber, pageSize, ShipTo.class.getSimpleName(), getEntityManager());

		return results;
	}

	@Override
	public ShipTo getShipTo(Long id) {
		return getEntityManager().find(ShipTo.class, id);
	}

	@Override
	@Transactional
	public ShipTo saveShipTo(ShipTo ShipTo) {
		getEntityManager().persist(ShipTo);
		getEntityManager().flush();
		return ShipTo;
	}

	@Override
	@Transactional
	public ShipTo updateShipTo(ShipTo ShipTo) {
		ShipTo = getEntityManager().merge(ShipTo);
		getEntityManager().flush();
		return ShipTo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShipTo> filter(String keyword) {
		if (keyword == null || keyword.isEmpty())
			return null;
		return getEntityManager().createNamedQuery("ShipTo.filterByAddressMatch")
				.setParameter("keyword", "%" + keyword.toLowerCase() + "%").getResultList();
	}

	@Override
	@Transactional
	public String delete(Long id) {
		ShipTo shipTo = getShipTo(id);
		if (shipTo != null) {
			shipTo.setArchived(true);
			updateShipTo(shipTo);
			return "success";
		}
		return "fail";
	}

}
